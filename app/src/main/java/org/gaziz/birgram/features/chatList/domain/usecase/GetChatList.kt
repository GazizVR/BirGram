package org.gaziz.birgram.features.chatList.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.core.ui.usecase.GetChatAvatar
import org.gaziz.birgram.features.chatList.domain.mapper.formatChatTime
import org.gaziz.birgram.features.chatList.domain.model.ChatListItem
import org.gaziz.telegram.api.ChatService
import org.gaziz.telegram.api.UserService
import org.gaziz.telegram.api.model.chat.Chat
import org.gaziz.telegram.api.model.chat.ChatListType
import org.gaziz.telegram.api.model.chat.ChatPosition
import org.gaziz.telegram.api.model.chat.ChatType
import org.gaziz.telegram.api.model.message.DraftMessageContent
import org.gaziz.telegram.api.model.message.MessageSender
import org.gaziz.telegram.api.model.user.UserStatus
import org.gaziz.telegram.api.model.user.UserType
import java.time.LocalDateTime
import javax.inject.Inject

class GetChatList @Inject constructor(
    private val chatService: ChatService,
    private val userService: UserService,
    private val getChatAvatar: GetChatAvatar
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
                    val draftMsg = chat.draftMessage
                    val isDraftMsg =
                        draftMsg != null &&
                        draftMsg.content is DraftMessageContent.Text

                    var sender: String? = null
                    val chatType = chat.type
                    val lastMsg = chat.lastMessage
                    if(
                        (chatType is ChatType.BasicGroup ||
                        (chatType is ChatType.SuperGroup && !chatType.isChannel)) &&
                        lastMsg?.sender is MessageSender.User
                    ) {
                        val user = userService.users.value[(lastMsg.sender as MessageSender.User).id]
                        if(user != null) {
                            sender = user.firstName
                        }
                    }
                    val isDeleted = (
                        chatType is ChatType.Private &&
                        userService.users.value[chatType.userId]?.type !is UserType.Regular &&
                        userService.users.value[chatType.userId]?.type !is UserType.Bot
                    )
                    val avatar = getChatAvatar(chat)
                    ChatListItem(
                        id = chat.id,
                        title = chat.title,
                        lastMessage = chat.lastMessage,
                        draftMessage = chat.draftMessage,
                        unreadCount = chat.unreadCount,
                        mentionCount = chat.mentionCount,
                        reactionCount = chat.reactionCount,
                        isDeleted = isDeleted,
                        lastMsgDate = if (isDraftMsg) {
                            draftMsg.date.formatChatTime()
                        } else {
                            (chat.lastMessage?.date ?: LocalDateTime.now()).formatChatTime()
                        },
                        avatar = avatar,
                        isDraftMsg = isDraftMsg,
                        isOnline = (
                            chatType is ChatType.Private &&
                            userService.users.value[chatType.userId]?.status is UserStatus.Online &&
                            userService.users.value[chatType.userId]?.type is UserType.Regular
                        ) || (
                            chatType is ChatType.Secret &&
                            userService.users.value[chatType.userId]?.status is UserStatus.Online &&
                            userService.users.value[chatType.userId]?.type is UserType.Regular
                        ),
                        messageSender = sender
                    )
                }
        }
    }
}