package org.gaziz.birgram.feature.chat.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.gaziz.birgram.feature.chat.R
import org.gaziz.birgram.feature.chat.ui.component.ChatTopBar
import org.gaziz.birgram.feature.chat.ui.component.MessageCard
import org.gaziz.birgram.feature.chat.ui.component.MessageInputBar
import org.gaziz.birgram.feature.chat.ui.component.ScrollDownButton
import org.gaziz.birgram.feature.chat.ui.component.TextBox
import org.gaziz.birgram.feature.chat.ui.model.AvatarUiState
import org.gaziz.birgram.feature.chat.ui.model.TitleUiState

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val messages by viewModel.messages.collectAsState()
    val chat by viewModel.chat.collectAsState()
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
                        messages.values.lastOrNull()?.lastOrNull()?.let { msg ->
                            viewModel.loadMessages(msg.id)
                        }
                    }
                }
            }
    }
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
    var listSize = remember { 0 }
    LaunchedEffect(Unit) {
        viewModel.messages
            .map {
                val list = it.values.firstOrNull()
                val first = list?.size ?: 0
                val second = list?.firstOrNull()?.isOutgoing ?: false
                first to second
            }
            .distinctUntilChanged()
            .debounce(100)
            .collect { (size,isOutgoing) ->
                if(listSize < size) {
                    if(!isOutgoing){
                        isScrollDownButton = listState.firstVisibleItemIndex > 0
                    }
                    if (isOutgoing || listState.firstVisibleItemIndex < 3) {
                        listSize = size
                        listState.animateScrollToItem(0)
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
                }
            } else {
                val noMessagesYet = stringResource(R.string.no_messages_yet)
                TextBox(
                    text = noMessagesYet,
                    modifier = Modifier.fillMaxSize().background(containerColor),
                    fontSize = fontSize
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                AnimatedVisibility(
                    visible = isScrollDownButton,
                    enter = slideInVertically(tween()) { it } + scaleIn(),
                    exit = slideOutVertically(tween()) { it } + scaleOut()
                ) {
                    val scope = rememberCoroutineScope()
                    ScrollDownButton(
                        onClick = {
                            scope.launch {
                                listState.animateScrollToItem(0)
                                isScrollDownButton = false
                            }
                        },
                        unreadCount = chat?.unreadCount
                    )
                }
            }
        }
    }
}