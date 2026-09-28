package org.gaziz.birgram.core.telegram.ui.mapper

import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.message.MessageSender
import org.gaziz.birgram.core.telegram.api.model.user.User
import org.gaziz.birgram.core.telegram.ui.model.MessageSenderUiState
import org.gaziz.birgram.core.telegram.ui.provider.ChatAvatarProvider
import org.gaziz.birgram.core.telegram.ui.provider.UserAvatarProvider
import javax.inject.Inject

class MessageSenderUiMapper @Inject constructor(
    private val chatAvatarUiMapper: ChatAvatarProvider,
    private val userAvatarUiMapper: UserAvatarProvider,
    private val accentColorMapper: AccentColorMapper
) {
    operator fun invoke(
        messageSender: MessageSender,
        chatById: Map<Long, Chat>,
        usersById: Map<Long, User>
    ): MessageSenderUiState? {
        return when(messageSender) {
            is MessageSender.Chat -> {
                val chat = chatById[messageSender.id] ?: return null
                val accentColor = accentColorMapper(chat.accentColorId)
                MessageSenderUiState(
                    name = chat.title,
                    avatar = chatAvatarUiMapper(chat,chatById,usersById),
                    accentColor = accentColor
                )
            }
            is MessageSender.User -> {
                val user = usersById[messageSender.id] ?: return null
                val accentColor = accentColorMapper(user.accentColorId)
                MessageSenderUiState(
                    name = user.firstName,
                    avatar = userAvatarUiMapper(user),
                    accentColor = accentColor
                )
            }
            else -> null
        }
    }
}