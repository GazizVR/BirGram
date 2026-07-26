package org.gaziz.birgram.features.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import org.gaziz.birgram.R
import org.gaziz.birgram.features.chat.ui.component.ChatTopBar
import org.gaziz.birgram.features.chat.ui.component.MessageCard
import org.gaziz.birgram.features.chat.ui.component.MessageInputBar
import org.gaziz.birgram.features.chat.ui.component.TextBox
import org.gaziz.birgram.features.chat.ui.model.AvatarUiState
import org.gaziz.birgram.features.chat.ui.model.TitleUiState

@Composable
fun ChatScreen(
    chatId: Long,
    onBack: () -> Unit
) {
    val viewModel = hiltViewModel<ChatViewModel>()
    DisposableEffect(Unit) {
        viewModel.openChat(chatId)
        onDispose { viewModel.closeChat(chatId) }
    }
    val chat by viewModel.chat(chatId).collectAsState()
    val messages by viewModel.messages(chatId).collectAsState()
    val containerColor = MaterialTheme.colorScheme.surfaceContainer
    val listState = rememberLazyListState()
    LaunchedEffect(Unit) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index to listState.layoutInfo.totalItemsCount
        }
            .distinctUntilChanged()
            .collect { (lastItem,total) ->
                if(lastItem != null) {
                    if(total-lastItem <= 10) {
                        messages.values.lastOrNull()?.last()?.let { msg ->
                            viewModel.loadMessages(chatId,msg.id)
                        }
                    }
                }
            }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(containerColor),
        topBar = {
            val deletedAccount = stringResource(R.string.deleted_account)
            ChatTopBar(
                avatar = AvatarUiState(
                    avatar = chat?.avatar,
                    size = 40.dp
                ),
                title = TitleUiState(
                    title = if(chat?.isDeleted == true) deletedAccount else chat?.title,
                    fontSize = 6.sp
                ),
                info = chat?.typeInfo,
                onBackClick = onBack,
                onMoreClick = {}
            )
        },
        bottomBar = {
            val height = 60.dp
            MessageInputBar(
                modifier = Modifier.height(height),
                fontSize = 8.sp,
                sendMessage = {}
            )
        },
    ) { paddingValues ->
        val fontSize = 6.sp
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(containerColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            reverseLayout = true
        ) {
            if(messages.isNotEmpty()) {
                messages.forEach { (date, messages) ->
                    items(
                        items = messages,
                        key = { it.id }
                    ) { msg ->
                        MessageCard(
                            message = msg,
                            fontSize = 6.sp
                        )
                    }
                    item {
                        TextBox(
                            text = date,
                            modifier = Modifier.fillMaxSize().background(containerColor),
                            fontSize = fontSize
                        )
                    }
                }
            } else {
                item {
                    val noMessagesYet = stringResource(R.string.no_messages_yet)
                    TextBox(
                        text = noMessagesYet,
                        modifier = Modifier.fillMaxSize().background(containerColor),
                        fontSize = fontSize
                    )
                }
            }
        }
    }
}