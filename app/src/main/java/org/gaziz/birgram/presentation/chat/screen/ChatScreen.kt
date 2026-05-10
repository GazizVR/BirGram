package org.gaziz.birgram.presentation.chat.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import org.gaziz.birgram.domain.model.chat.ChatType
import org.gaziz.birgram.presentation.chat.components.ChatTopBar
import org.gaziz.birgram.presentation.chat.components.MessageCard
import org.gaziz.birgram.presentation.chat.viewmodel.ChatViewModel

@Composable
fun ChatScreen(
    chatId: Long,
    onBack: () -> Unit
){
    val viewModel = hiltViewModel<ChatViewModel>()
    val chat by viewModel.getChat(chatId).collectAsState()
    val messages by viewModel.chatMessages(chatId).collectAsState()
    val containerColor = CardDefaults.cardColors().containerColor
    val lazyListState = rememberLazyListState()

    DisposableEffect(Unit) {
        viewModel.openChat(chatId)
        onDispose { viewModel.closeChat(chatId) }
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index to
                    lazyListState.layoutInfo.totalItemsCount
        }
            .distinctUntilChanged()
            .collect { (lastVisible, total) ->
                if (
                    lastVisible != null &&
                    total > 0 &&
                    lastVisible >= total - 5
                ) {
                    viewModel.loadMessages(chatId, messages.lastOrNull()?.id ?: 0)
                }
            }
    }

    Scaffold(
        topBar = {
            if(chat != null)  {
                ChatTopBar(
                    onBack = onBack,
                    photo = chat?.photo,
                    title = chat?.title ?: "",
                    type = chat?.type ?: ChatType.Other
                )
            }
        }
    ) {
        if(messages.isNotEmpty()) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(containerColor),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                reverseLayout = true
            ) {
                items(messages) { msg ->
                    MessageCard(msg)
                }
            }
        }
    }
}