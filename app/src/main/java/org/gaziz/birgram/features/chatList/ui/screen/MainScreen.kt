package org.gaziz.birgram.features.chatList.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.ui.icons.archive
import org.gaziz.birgram.core.ui.icons.skull
import org.gaziz.birgram.features.chatList.ui.ChatListViewModel
import org.gaziz.birgram.features.chatList.ui.component.ChatCard
import org.gaziz.birgram.features.chatList.ui.component.chatCard.LastMessagePreview
import org.gaziz.birgram.features.chatList.ui.component.MainScreenMenu
import org.gaziz.birgram.features.chatList.ui.component.MainScreenTopBar
import org.gaziz.birgram.features.chatList.ui.model.CardPhoto
import org.gaziz.birgram.features.chatList.ui.model.CardText
import org.gaziz.birgram.features.chatList.ui.model.CardUnreadBadge

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

    val users by viewModel.users.collectAsState()
    val window = LocalWindowInfo.current
    val cardHeight = 70.dp
    val cardWidth = window.containerDpSize.width
    val cardPhotoSize = 54.dp

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
                    modifier = Modifier.fillMaxSize().padding(it),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    item {
                        if(archivedChats.isNotEmpty()) {
                            val archivedChats = stringResource(R.string.archived_chats)
                            ChatCard(
                                modifier = Modifier
                                    .height(cardHeight)
                                    .width(cardWidth),
                                photo = CardPhoto(
                                    model = archive,
                                    size = cardPhotoSize,
                                    placeHolderColor = MaterialTheme.colorScheme.onSurface.copy(0.25f),
                                ),
                                title = CardText(
                                    text = archivedChats,
                                    fontSize = 7.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                ),
                                onClick = onArchiveClick
                            )
                        }
                    }
                    items(mainChats) { chat ->
                        val accentColor by viewModel.getAccentColor(chat.accentColorId).collectAsState()
                        val isDeleted =
                            users[(chat.type as? ChatType.Private)?.userId]?.type is UserType.Deleted ||
                            users[(chat.type as? ChatType.Private)?.userId]?.type is UserType.Unknown
                        ChatCard(
                            modifier = Modifier
                                .height(cardHeight)
                                .width(cardWidth),
                            photo = CardPhoto(
                                model = if(isDeleted) skull else chat.photo,
                                size = cardPhotoSize,
                                placeHolderColor = accentColor,
                                onNull = { fileId -> viewModel.downloadChatIcon(chat.id,fileId) },
                            ),
                            title = CardText(
                                text = if(isDeleted) stringResource(R.string.deleted_account) else chat.title,
                                fontSize = 7.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            lastMessage = {
                                LastMessagePreview(
                                    draftMessage = chat.draftMessage,
                                    lastMessage = chat.lastMessage,
                                    fontSize = 6.sp
                                )
                            },
                            unreadBadge = CardUnreadBadge(
                                unreadCount = chat.unreadCount,
                                mentionCount = chat.mentionCount,
                                reactionCount = chat.reactionCount,
                                fontSize = 5.sp
                            ),
                            onClick = { onChatClick(chat.id) }
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