package org.gaziz.birgram.features.chatList.domain.usecase

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.chat.ChatListType
import org.gaziz.birgram.core.telegram.api.model.chat.ChatPosition
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessageContent
import org.gaziz.birgram.core.telegram.api.model.message.MessageSender
import org.gaziz.birgram.core.telegram.api.model.user.UserStatus
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.telegram.api.usecase.DownloadChatPhotoSmall
import org.gaziz.birgram.core.telegram.api.usecase.GetAccentColorById
import org.gaziz.birgram.core.ui.icon.skull
import org.gaziz.birgram.features.chatList.domain.mapper.toStringDate
import org.gaziz.birgram.core.ui.model.ChatAvatar
import org.gaziz.birgram.features.chatList.domain.model.ChatListItem
import java.time.LocalDateTime
import javax.inject.Inject

class GetChatList @Inject constructor(
    private val chatService: ChatService,
    private val getAccentColorById: GetAccentColorById,
    private val userService: UserService,
    private val downloadChatPhotoSmall: DownloadChatPhotoSmall
) {
    operator fun invoke(type: ChatListType): Flow<List<ChatListItem>> {
        return chatService.chats.map { map ->
            map.values
                .mapNotNull { chat ->
                    val position = chat.positions.find { it.listType == type } ?: return@mapNotNull null
                    chat to position
                }
                .sortedWith(
                    compareByDescending<Pair<Chat, ChatPosition>> { it.second.isPinned }
                        .thenByDescending { it.second.order }
                )
                .map {
                    val chat = it.first
                    val accentColor = getAccentColorById(chat.accentColorId).stateIn(
                        CoroutineScope(Dispatchers.IO)
                    )
                    val isDraftMsg =
                        chat.draftMessage != null &&
                        chat.draftMessage.content is DraftMessageContent.Text &&
                        !chat.draftMessage.content.clearDraft

                    var sender: String? = null
                    if(
                        (chat.type is ChatType.BasicGroup ||
                        (chat.type is ChatType.SuperGroup && !chat.type.isChannel)) &&
                        chat.lastMessage?.sender is MessageSender.User
                    ) {
                        val user = userService.users.value[chat.lastMessage.sender.id]
                        if(user != null) {
                            sender = user.firstName
                        }
                    }
                    val isDeleted = (
                        chat.type is ChatType.Private &&
                        userService.users.value[chat.type.userId]?.type !is UserType.Regular &&
                        userService.users.value[chat.type.userId]?.type !is UserType.Bot
                    )
                    ChatListItem(
                        chat = chat,
                        isDeleted = isDeleted,
                        lastMsgDate = if(isDraftMsg) chat.draftMessage.date.toStringDate() else (chat.lastMessage?.date ?: LocalDateTime.now()).toStringDate(),
                        avatar = when {
                            isDeleted -> {
                                ChatAvatar.Icon(
                                    imageVector = skull,
                                    background = accentColor.value
                                )
                            }
                            chat.photo != null && chat.photo.small.path.isNotBlank() -> {
                                val bitmap = BitmapFactory.decodeFile(chat.photo.small.path)
                                val image = bitmap.asImageBitmap()
                                ChatAvatar.Photo(image)
                            }
                            chat.photo != null && chat.photo.miniThumbnail != null -> {
                                ChatAvatar.Photo(
                                    bitmap = chat.photo.miniThumbnail.decodeToImageBitmap(),
                                    onEmpty = {
                                        downloadChatPhotoSmall(chat.id,chat.photo.small.id)
                                    }
                                )
                            }
                            else -> ChatAvatar.PlaceHolder(
                                text = if(chat.title.isNotBlank()) chat.title[0].toString() else "",
                                color = accentColor.value,
                                downloadPhoto = {
                                    if(chat.photo != null) {
                                        downloadChatPhotoSmall(chat.id,chat.photo.small.id)
                                    }
                                }
                            )
                        },
                        isDraftMsg = isDraftMsg,
                        isOnline = (
                            chat.type is ChatType.Private &&
                            userService.users.value[chat.type.userId]?.status is UserStatus.Online &&
                            userService.users.value[chat.type.userId]?.type is UserType.Regular
                        ) || (
                            chat.type is ChatType.Secret &&
                            userService.users.value[chat.type.userId]?.status is UserStatus.Online &&
                                    userService.users.value[chat.type.userId]?.type is UserType.Regular
                        ),
                        messageSender = sender
                    )
                }
        }
    }
}