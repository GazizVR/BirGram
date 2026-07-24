package org.gaziz.birgram.features.chatList.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.gaziz.birgram.R
import org.gaziz.birgram.core.ui.icon.archive
import org.gaziz.birgram.core.ui.model.ChatAvatar
import org.gaziz.birgram.features.chatList.ui.ChatListViewModel
import org.gaziz.birgram.features.chatList.ui.component.ChatCard
import org.gaziz.birgram.features.chatList.ui.component.MainScreenMenu
import org.gaziz.birgram.features.chatList.ui.component.MainScreenTopBar
import org.gaziz.birgram.features.chatList.ui.component.chatCard.DraftMessagePreview
import org.gaziz.birgram.features.chatList.ui.component.chatCard.LastMessagePreview
import org.gaziz.birgram.features.chatList.ui.component.chatCard.OnlineIndicator
import org.gaziz.birgram.features.chatList.ui.model.CardTextUiState
import org.gaziz.birgram.features.chatList.ui.model.LastMsgUiState
import org.gaziz.birgram.features.chatList.ui.model.PhotoUiState
import org.gaziz.birgram.features.chatList.ui.model.UnreadBadgeUiState

@Composable
fun MainScreen(
    onSearchClick: () -> Unit,
    onChatClick: (Long) -> Unit,
    onArchiveClick: () -> Unit
) {
    val viewModel = hiltViewModel<ChatListViewModel>()
    val mainChats by viewModel.mainChatList.collectAsState()
    val archivedChats by viewModel.archiveChatList.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isDark by viewModel.isDark.collectAsState()
    var isLogOut by rememberSaveable { mutableStateOf(false) }

    val window = LocalWindowInfo.current
    val cardHeight = 70.dp
    val cardWidth = window.containerDpSize.width
    val cardPhotoSize = 54.dp
    val cardColor = MaterialTheme.colorScheme.surfaceContainer

    val deletedAccount = stringResource(R.string.deleted_account)

    Box {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                MainScreenMenu(
                    isDark = isDark,
                    switchIsDark = { viewModel.switchIsDark(it) },
                    onLogOut = { isLogOut = true }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    MainScreenTopBar(
                        onSearchClick
                    ) {
                        scope.launch { drawerState.open() }
                    }
                }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    item {
                        if(archivedChats.isNotEmpty()) {
                            val archivedChats = stringResource(R.string.archived_chats)
                            ChatCard(
                                modifier = Modifier
                                    .height(cardHeight)
                                    .width(cardWidth)
                                    .background(cardColor),
                                photo = PhotoUiState(
                                    photo = ChatAvatar.Icon(
                                        imageVector = archive,
                                        background = MaterialTheme.colorScheme.onSurface.copy(0.25f)
                                    ),
                                    size = cardPhotoSize,
                                ),
                                title = CardTextUiState(
                                    text = archivedChats,
                                    fontSize = 7.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                ),
                                onClick = onArchiveClick
                            )
                        }
                    }
                    items(mainChats) { item ->
                        ChatCard(
                            modifier = Modifier
                                .height(cardHeight)
                                .width(cardWidth)
                                .background(cardColor),
                            photo = PhotoUiState(
                                photo = item.avatar,
                                size = cardPhotoSize,
                                overlay = {
                                    OnlineIndicator(
                                        size = 10.dp,
                                        isOnline = item.isOnline,
                                        indicatorColor = Color.Green,
                                        backgroundColor = CardDefaults.cardColors().containerColor,
                                        alignment = Alignment.BottomEnd,
                                    )
                                }
                            ),
                            title = CardTextUiState(
                                text = if(item.isDeleted) deletedAccount else item.chat.title,
                                fontSize = 7.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            lastMessage = LastMsgUiState(
                                component = { modifier ->
                                    if(item.isDraftMsg) {
                                        if(item.chat.draftMessage != null) {
                                            DraftMessagePreview(
                                                modifier = modifier,
                                                draftMessage = item.chat.draftMessage,
                                                fontSize = 6.sp
                                            )
                                        }
                                    } else {
                                        if(item.chat.lastMessage != null) {
                                            LastMessagePreview(
                                                modifier = modifier,
                                                lastMessage = item.chat.lastMessage,
                                                fontSize = 6.sp,
                                                sender = item.messageSender
                                            )
                                        }
                                    }
                                },
                                date = item.lastMsgDate,
                                fontSize = 6.sp
                            ),
                            unreadBadge = UnreadBadgeUiState(
                                unreadCount = item.chat.unreadCount,
                                mentionCount = item.chat.mentionCount,
                                reactionCount = item.chat.reactionCount,
                                fontSize = 5.sp
                            ),
                            onClick = { onChatClick(item.chat.id) },
                        )
                    }
                }
            }
        }
        if(isLogOut) {
            val cnt = stringArrayResource(R.array.log_out_cnt)
            AlertDialog(
                onDismissRequest = { isLogOut = false },
                title = { Text(cnt[0]) },
                text = { Text(cnt[1]) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            isLogOut = false
                            viewModel.logOut { scope.launch { drawerState.close() } }
                        }
                    ) {
                        Text(cnt[0], color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { isLogOut = false }) {
                        Text(cnt[2])
                    }
                }
            )
        }
    }
}