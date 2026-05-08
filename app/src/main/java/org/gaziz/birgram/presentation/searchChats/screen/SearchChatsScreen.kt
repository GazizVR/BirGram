package org.gaziz.birgram.presentation.searchChats.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.gaziz.birgram.presentation.searchChats.components.SearchChatCard
import org.gaziz.birgram.presentation.searchChats.components.SearchTopBar
import org.gaziz.birgram.presentation.searchChats.viewmodel.SearchChatsViewModel

@Composable
fun SearchChatsScreen(
    onBack: () -> Unit
){
    val viewModel = hiltViewModel<SearchChatsViewModel>()
    val chats by viewModel.searchChats.collectAsState()
    Scaffold(
        topBar = { SearchTopBar(onBack) {viewModel.sendSearchQuery(it)} }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(chats.toList()) { chat ->
                SearchChatCard(chat.second) {viewModel.downloadPhoto(it)}
            }
        }
    }
}