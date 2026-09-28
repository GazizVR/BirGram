package org.gaziz.birgram.core.ui.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.message.MessageSender
import org.gaziz.birgram.core.ui.model.MessageSenderInfo
import javax.inject.Inject

class GetMessageSenderInfo @Inject constructor(
    private val chatService: ChatService,
    private val userService: UserService,
    private val getChatAvatar: GetChatAvatar,
    private val getUserAvatar: GetUserAvatar,
    private val getAccentColorById: GetAccentColorById
) {
    operator fun invoke(
        messageSender: MessageSender
    ): Flow<MessageSenderInfo?> {
        return when(messageSender) {
            is MessageSender.Chat -> {
                chatService.chats.map {
                    val chat = it[messageSender.id] ?: return@map null
                    val accentColor = getAccentColorById(chat.accentColorId)
                    MessageSenderInfo(
                        name = chat.title,
                        avatar = getChatAvatar(chat),
                        accentColor = accentColor
                    )
                }
            }
            is MessageSender.User -> {
                userService.users.map {
                    val user = it[messageSender.id] ?: return@map null
                    val accentColor = getAccentColorById(user.accentColorId)
                    MessageSenderInfo(
                        name = user.firstName,
                        avatar = getUserAvatar(user),
                        accentColor = accentColor
                    )
                }
            }
            else -> flowOf(null)
        }
    }
}