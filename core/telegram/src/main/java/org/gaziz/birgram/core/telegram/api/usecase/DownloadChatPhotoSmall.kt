package org.gaziz.birgram.core.telegram.api.usecase

import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.model.media.ProfilePhoto
import javax.inject.Inject

class DownloadChatPhotoSmall @Inject constructor(
    private val downloadOrGetFileDataById: DownloadOrGetFileDataById,
    private val chatService: ChatService
) {
    operator fun invoke(
        fileId: Int,
        chatId: Long
    ) {
        downloadOrGetFileDataById(
            fileId = fileId,
            onFile = { file ->
                chatService.updateChats { old ->
                    val chat = old[chatId] ?: return@updateChats old
                    var newPhoto = ProfilePhoto(
                        miniThumbnail = null,
                        small = file
                    )
                    chat.photo?.let { photo -> newPhoto = photo.copy(small = file) }
                    old + (chatId to chat.copy(photo = newPhoto))
                }
            }
        )
    }
}