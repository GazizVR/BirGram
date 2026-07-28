package org.gaziz.birgram.core.telegram.api.usecase

import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.message.MessageSender
import org.gaziz.birgram.core.telegram.api.model.message.MessageSenderInfo
import javax.inject.Inject

class GetMessageSenderInfo @Inject constructor(
    private val chatService: ChatService,
    private val userService: UserService,
    private val getChatAvatar: GetChatAvatar,
    private val getUserAvatar: GetUserAvatar
) {
    suspend operator fun invoke(
        messageSender: MessageSender
    ): MessageSenderInfo? {
        return when(messageSender) {
            is MessageSender.Chat -> {
                val chat = chatService.chats.value[messageSender.id] ?: return null
                MessageSenderInfo(
                    name = chat.title,
                    avatar = getChatAvatar(chat)
                )
            }
            is MessageSender.User -> {
                val user = userService.users.value[messageSender.id] ?: return null
                MessageSenderInfo(
                    name = user.firstName,
                    avatar = getUserAvatar(user)
                )
            }
            else -> null
        }
    }
}