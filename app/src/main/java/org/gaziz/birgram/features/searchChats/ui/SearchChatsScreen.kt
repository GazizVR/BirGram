package org.gaziz.birgram.features.searchChats.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.gaziz.birgram.R
import org.gaziz.birgram.features.searchChats.ui.components.SearchTopBar
import org.gaziz.birgram.features.searchChats.ui.components.SearchedCard
import org.gaziz.birgram.features.searchChats.ui.model.PhotoUiState
import org.gaziz.birgram.features.searchChats.ui.model.TextUiState

@Composable
fun SearchChatsScreen(
    onBack: () -> Unit,
    onChatClick: (Long) -> Unit
){
    val viewModel = hiltViewModel<SearchChatsViewModel>()
    val chats by viewModel.searchChats.collectAsState()
    val emptySearch = stringResource(R.string.empty_search)
    Scaffold(
        topBar = { SearchTopBar(onBack) {viewModel.sendSearchQuery(it)} }
    ) { paddingValues ->
        if(chats.isNotEmpty()) {
            val cardHeight = 60.dp
            val cardColor = MaterialTheme.colorScheme.surfaceContainer
            val cardPhotoSize = 44.dp
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(chats.toList()) { item ->
                    SearchedCard(
                        modifier = Modifier
                            .height(cardHeight)
                            .background(cardColor),
                        photo = PhotoUiState(
                            avatar = item.second.avatar,
                            size = cardPhotoSize
                        ),
                        title = TextUiState(
                            text = item.second.chat.title,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 6.sp
                        ),
                        onClick = { onChatClick(item.first) }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emptySearch,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}