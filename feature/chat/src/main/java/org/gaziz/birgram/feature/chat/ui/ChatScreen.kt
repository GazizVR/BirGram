package org.gaziz.birgram.feature.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.mapNotNull
import org.gaziz.birgram.core.telegram.api.model.message.MessageSendingState
import org.gaziz.birgram.core.telegram.ui.model.ChatTypeUiState
import org.gaziz.birgram.feature.chat.R
import org.gaziz.birgram.feature.chat.ui.component.MessageCardWrapper
import org.gaziz.birgram.feature.chat.ui.component.TextBox
import org.gaziz.birgram.feature.chat.ui.component.bar.ChatTopBar
import org.gaziz.birgram.feature.chat.ui.component.bar.MessageInputBar
import org.gaziz.birgram.feature.chat.ui.component.button.ScrollDownButtonWrapper
import org.gaziz.birgram.feature.chat.ui.component.dialog.DeleteMessageDialog
import org.gaziz.birgram.feature.chat.ui.component.menu.MessageActionMenu
import org.gaziz.birgram.feature.chat.ui.model.ChatAvatarUiState
import org.gaziz.birgram.feature.chat.ui.model.TitleUiState

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val messages by viewModel.messages.collectAsState()
    val chat by viewModel.chat.collectAsState()
    val containerColor = MaterialTheme.colorScheme.surfaceContainer
    // Message pagination
    val listState = rememberLazyListState()
    LaunchedEffect(Unit) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index to listState.layoutInfo.totalItemsCount
        }
            .distinctUntilChanged()
            .collect { (lastItem,total) ->
                if(lastItem != null) {
                    if(total-lastItem <= 10) {
                        messages.values.lastOrNull()?.lastOrNull()?.let { msg ->
                            viewModel.loadMessages(msg.id)
                        }
                    }
                }
            }
    }
    // Scroll down button visibility
    var isScrollDownButton by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        var previousIndex = listState.firstVisibleItemIndex
        var previousOffset = listState.firstVisibleItemScrollOffset
        snapshotFlow {
            Triple(
                listState.isScrollInProgress,
                listState.firstVisibleItemIndex,
                listState.firstVisibleItemScrollOffset
            )
        }
            .distinctUntilChanged()
            .collect { (isScroll,currentIndex,currentOffset) ->
                if(listState.firstVisibleItemIndex == 0) {
                    isScrollDownButton = false
                } else {
                    if(isScroll) {
                        isScrollDownButton = when {
                            currentIndex > previousIndex -> false
                            currentIndex < previousIndex -> true
                            currentOffset > previousOffset -> false
                            currentOffset < previousOffset -> true
                            else -> isScrollDownButton
                        }
                        previousIndex = currentIndex
                        previousOffset = currentOffset
                    }
                }
            }
    }
    // Auto-scroll on new messages
    LaunchedEffect(Unit) {
        viewModel.chat
            .mapNotNull { it?.lastMessage }
            .distinctUntilChangedBy { it.id }
            .debounce(100)
            .collect { msg ->
                val isOutgoing = msg.isOutgoing
                if (isOutgoing || listState.firstVisibleItemIndex < 3) {
                    listState.animateScrollToItem(0)
                }
            }
    }
    // UI Content
    var deleteMessageIds by rememberSaveable { mutableStateOf<LongArray?>(null) }
    var selectedMessageId by remember { mutableStateOf<Long?>(null) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(containerColor),
        topBar = {
            val deletedAccount = stringResource(R.string.deleted_account)
            ChatTopBar(
                avatar = ChatAvatarUiState(
                    avatar = chat?.avatar,
                    size = 40.dp
                ),
                title = TitleUiState(
                    title = if (chat?.isDeleted == true) deletedAccount else chat?.title,
                    fontSize = 6.sp
                ),
                info = chat?.typeInfo,
                onBackClick = onBack,
                onMoreClick = {}
            )
        },
        bottomBar = {
            chat?.let { c ->
                if(c.canSendTextMessages) {
                    MessageInputBar(
                        modifier = Modifier.imePadding(),
                        defaultText = c.draftText,
                        fontSize = 8.sp,
                        sendMessage = { viewModel.sendMessageText(it) },
                        setDraft = { viewModel.setDraftMessageText(it) }
                    )
                }
            }
        }
    ) { paddingValues ->
        val fontSize = 6.sp
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (messages.isNotEmpty()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(containerColor),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    reverseLayout = true
                ) {
                    messages.forEach { (date, messages) ->
                        items(
                            items = messages,
                            key = { it.id }
                        ) { msg ->
                            MessageCardWrapper(
                                message = msg,
                                fontSize = 6.sp,
                                onFirstClick = { viewModel.loadMessageProperties(msg.id) },
                                onClick = { selectedMessageId = msg.id }
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
                }
            } else {
                val noMessagesYet = stringResource(R.string.no_messages_yet)
                TextBox(
                    text = noMessagesYet,
                    modifier = Modifier.fillMaxSize().background(containerColor),
                    fontSize = fontSize
                )
            }
            ScrollDownButtonWrapper(
                visible = isScrollDownButton,
                unreadCount = chat?.unreadCount,
                onClick = {
                    listState.animateScrollToItem(0)
                    isScrollDownButton = false
                }
            )
        }
    }
    val messagesById by viewModel.messagesById.collectAsState()
    if(selectedMessageId != null) {
        val selectedMessage by remember(messagesById) {
            derivedStateOf { messagesById[selectedMessageId] }
        }
        if(selectedMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f)
                    .background(MaterialTheme.colorScheme.background.copy(0.15f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        selectedMessageId = null
                    }
            ) {
                MessageActionMenu(
                    msg = selectedMessage!!,
                    onDismissRequest = { selectedMessageId = null },
                    onDelete = { msgId ->
                        if (selectedMessage!!.sendingState is MessageSendingState.Pending) {
                            viewModel.deleteMessages(
                                LongArray(1) { msgId },
                                selectedMessage!!.canDeleteForAll
                            )
                        } else {
                            deleteMessageIds = LongArray(1) { msgId }
                        }
                    },
                    onRetry = { msgId ->
                        viewModel.resendMessages(
                            LongArray(1) { msgId }
                        )
                    }
                )
            }
        }
    }
    val othersStr = stringResource(R.string.others)
    DeleteMessageDialog(
        deleteMessageIds = deleteMessageIds,
        onValueChange = { deleteMessageIds = it },
        user = when(chat?.typeInfo) {
            is ChatTypeUiState.User -> chat?.title ?: othersStr
            is ChatTypeUiState.BasicGroup -> othersStr
            else -> null
        },
        onDelete = { ids, forAll ->
            viewModel.deleteMessages(
                ids,
                forAll
            )
        }
    )
}