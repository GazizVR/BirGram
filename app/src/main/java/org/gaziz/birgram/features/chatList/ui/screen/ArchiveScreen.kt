package org.gaziz.birgram.features.chatList.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessageContent
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.ui.icons.skull
import org.gaziz.birgram.features.chatList.ui.ChatListViewModel
import org.gaziz.birgram.features.chatList.ui.component.ArchiveScreenTopBar
import org.gaziz.birgram.features.chatList.ui.component.ChatCard
import org.gaziz.birgram.features.chatList.ui.component.chatCard.DraftMessagePreview
import org.gaziz.birgram.features.chatList.ui.component.chatCard.LastMessagePreview
import org.gaziz.birgram.features.chatList.ui.model.CardTextUiState
import org.gaziz.birgram.features.chatList.ui.model.LastMsgUiState
import org.gaziz.birgram.features.chatList.ui.model.PhotoUiState
import org.gaziz.birgram.features.chatList.ui.model.UnreadBadgeUiState
import java.time.LocalDateTime

@Composable
fun ArchiveScreen(
    onBack: () -> Unit,
    onChatClick: (Long) -> Unit
) {
    val archivedChats = stringResource(R.string.archived_chats)
    val window = LocalWindowInfo.current
    val cardHeight = 70.dp
    val cardWidth = window.containerDpSize.width

    val viewModel = hiltViewModel<ChatListViewModel>()
    val chats by viewModel.archiveChatList.collectAsState()
    val users by viewModel.users.collectAsState()
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
            items(chats) { chat ->
                val accentColor by viewModel.getAccentColor(chat.accentColorId).collectAsState()
                val isDeleted =
                    users[(chat.type as? ChatType.Private)?.userId]?.type is UserType.Deleted ||
                            users[(chat.type as? ChatType.Private)?.userId]?.type is UserType.Unknown
                val isDraftMsg = chat.draftMessage != null && chat.draftMessage.content is DraftMessageContent.Text && !chat.draftMessage.content.clearDraft
                ChatCard(
                    modifier = Modifier
                        .height(cardHeight)
                        .width(cardWidth),
                    photo = PhotoUiState(
                        model = if (isDeleted) skull else chat.photo,
                        size = 54.dp,
                        placeHolderColor = accentColor,
                        onNull = { fileId -> viewModel.downloadChatIcon(chat.id, fileId) },
                    ),
                    title = CardTextUiState(
                        text = if (isDeleted) stringResource(R.string.deleted_account) else chat.title,
                        fontSize = 7.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    lastMessage = LastMsgUiState(
                        component = { modifier ->
                            if(isDraftMsg) {
                                DraftMessagePreview(
                                    modifier = modifier,
                                    draftMessage = chat.draftMessage,
                                    fontSize = 6.sp
                                )
                            } else {
                                if(chat.lastMessage != null) {
                                    LastMessagePreview(
                                        modifier = modifier,
                                        lastMessage = chat.lastMessage,
                                        fontSize = 6.sp
                                    )
                                }
                            }
                        },
                        date = if(isDraftMsg) {
                            chat.draftMessage.date
                        } else {
                            chat.lastMessage?.date ?: LocalDateTime.now()
                        },
                        fontSize = 6.sp
                    ),
                    unreadBadge = UnreadBadgeUiState(
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