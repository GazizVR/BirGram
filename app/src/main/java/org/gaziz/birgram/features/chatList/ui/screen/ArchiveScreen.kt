package org.gaziz.birgram.features.chatList.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.gaziz.birgram.R
import org.gaziz.birgram.features.chatList.ui.ChatListViewModel
import org.gaziz.birgram.features.chatList.ui.component.ArchiveScreenTopBar
import org.gaziz.birgram.features.chatList.ui.component.ChatCard
import org.gaziz.birgram.features.chatList.ui.component.chatCard.DraftMessagePreview
import org.gaziz.birgram.features.chatList.ui.component.chatCard.LastMessagePreview
import org.gaziz.birgram.features.chatList.ui.component.chatCard.OnlineIndicator
import org.gaziz.birgram.features.chatList.ui.model.CardTextUiState
import org.gaziz.birgram.features.chatList.ui.model.LastMsgUiState
import org.gaziz.birgram.features.chatList.ui.model.PhotoUiState
import org.gaziz.birgram.features.chatList.ui.model.UnreadBadgeUiState

@Composable
fun ArchiveScreen(
    onBack: () -> Unit,
    onChatClick: (Long) -> Unit
) {
    val archivedChats = stringResource(R.string.archived_chats)
    val window = LocalWindowInfo.current
    val cardHeight = 70.dp
    val cardWidth = window.containerDpSize.width

    val cardPhotoSize = 54.dp
    val cardColor = MaterialTheme.colorScheme.surfaceContainer

    val deletedAccount = stringResource(R.string.deleted_account)

    val viewModel = hiltViewModel<ChatListViewModel>()
    val chats by viewModel.archiveChatList.collectAsState()

    Scaffold(
        topBar = {
            ArchiveScreenTopBar(
                title = archivedChats,
                onBackClick = onBack,
                onMoreClick = {}
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(chats) { item ->
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