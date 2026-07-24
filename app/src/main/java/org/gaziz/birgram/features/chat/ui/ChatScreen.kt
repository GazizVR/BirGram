package org.gaziz.birgram.features.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessageContent
import org.gaziz.birgram.features.chat.ui.components.ChatTopBar
import org.gaziz.birgram.features.chat.ui.components.MessageCard
import org.gaziz.birgram.features.chat.ui.components.MessageInputBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.formatMessagesList(locale: Locale = Locale.getDefault()): String {
    val now = LocalDate.now()

    return when {
        this.year == now.year -> format(DateTimeFormatter.ofPattern("d MMM", locale))
        else -> format(DateTimeFormatter.ofPattern("d MMM, yyyy", locale))
    }
}

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
    val noMessages = stringResource(R.string.no_messages)
    val focusManager = LocalFocusManager.current

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
                    lastVisible >= total - 10
                ) {
                    val messageId = messages.values.lastOrNull()?.lastOrNull()?.id ?: 0
                    viewModel.loadMessages(
                        chatId,
                        messageId
                    )
                }
            }
    }

    Scaffold(
        topBar = {
            if(chat != null)  {
                ChatTopBar(
                    onBack = {
                        focusManager.clearFocus()
                        onBack()
                    },
                    title = chat?.title ?: "",
                )
            }
        },
        bottomBar = {
            if(chat?.permissions?.canSendBasicMessages == true) MessageInputBar(
                defaultText = if(chat?.draftMessage != null) {
                   if(chat?.draftMessage?.content is DraftMessageContent.Text) {
                       (chat?.draftMessage?.content as DraftMessageContent.Text).text
                   } else {
                       ""
                   }
                } else {
                    ""
                },
                {viewModel.setDraftMessage(chatId,it)}
            ) {
                viewModel.sendMessage(chatId, it)
            }
        },
        modifier = Modifier.padding(WindowInsets.ime.asPaddingValues())
    ) {
        if(messages.isNotEmpty()) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(containerColor),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                reverseLayout = true,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                messages.forEach { (date, messages) ->
                    items(messages) { msg ->
                        MessageCard(msg)
                    }
                    item {
                        Text(
                            text = date.formatMessagesList(),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp
                        )
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.statusBarsPadding().fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text(
                        text = noMessages,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.sp
                    )
                }
            }
        }
    }
}