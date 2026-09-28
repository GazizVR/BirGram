package org.gaziz.birgram.core.telegram.ui.mapper

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.message.MessageSender
import org.gaziz.birgram.core.telegram.ui.model.MessageSenderUiState
import javax.inject.Inject

class MessageSenderUiMapper @Inject constructor(
    private val chatService: ChatService,
    private val userService: UserService,
    private val chatAvatarUiMapper: ChatAvatarUiMapper,
    private val userAvatarUiMapper: UserAvatarUiMapper,
    private val accentColorMapper: AccentColorMapper
) {
    operator fun invoke(
        messageSender: MessageSender
    ): Flow<MessageSenderUiState?> {
        return when(messageSender) {
            is MessageSender.Chat -> {
                chatService.chats.map {
                    val chat = it[messageSender.id] ?: return@map null
                    val accentColor = accentColorMapper(chat.accentColorId)
                    MessageSenderUiState(
                        name = chat.title,
                        avatar = chatAvatarUiMapper(chat),
                        accentColor = accentColor
                    )
                }
            }
            is MessageSender.User -> {
                userService.users.map {
                    val user = it[messageSender.id] ?: return@map null
                    val accentColor = accentColorMapper(user.accentColorId)
                    MessageSenderUiState(
                        name = user.firstName,
                        avatar = userAvatarUiMapper(user),
                        accentColor = accentColor
                    )
                }
            }
            else -> flowOf(null)
        }
    }
}