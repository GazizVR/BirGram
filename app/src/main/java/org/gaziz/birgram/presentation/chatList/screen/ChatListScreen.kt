package org.gaziz.birgram.presentation.chatList.screen

import androidx.activity.compose.BackHandler
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
import org.gaziz.birgram.presentation.chatList.components.ChatCard
import org.gaziz.birgram.presentation.chatList.components.ChatListTopBar
import org.gaziz.birgram.presentation.chatList.viewmodel.ChatListViewModel

@Composable
fun ChatListScreen() {
    BackHandler { }
    val viewModel = hiltViewModel<ChatListViewModel>()
    val chatList by viewModel.mainChatList.collectAsState()
    Scaffold(
        topBar = { ChatListTopBar() }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(it),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(chatList) { chat ->
                ChatCard(
                    chat,
                    { viewModel.downloadChatPhoto(it) }
                )
            }
        }
    }
}