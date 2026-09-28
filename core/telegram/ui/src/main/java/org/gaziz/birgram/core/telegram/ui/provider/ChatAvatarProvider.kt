package org.gaziz.birgram.core.telegram.ui.provider

import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.user.User
import org.gaziz.birgram.core.telegram.api.usecase.DownloadChatPhotoSmall
import org.gaziz.birgram.core.telegram.ui.mapper.ChatAvatarUiMapper
import org.gaziz.birgram.core.telegram.ui.model.AvatarUiState
import javax.inject.Inject

class ChatAvatarProvider @Inject constructor(
    private val chatAvatarUiMapper: ChatAvatarUiMapper,
    private val downloadChatPhotoSmall: DownloadChatPhotoSmall
) {
    operator fun invoke(
        chat: Chat,
        chatsById: Map<Long, Chat>,
        usersById: Map<Long, User>
    ): AvatarUiState {
        return chatAvatarUiMapper(
            chat = chat,
            chatsById = chatsById,
            usersById = usersById,
            onDownload = { downloadChatPhotoSmall(it,chat.id) }
        )
    }
}