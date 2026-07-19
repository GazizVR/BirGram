package org.gaziz.birgram.features.chatList.domain.usecase

import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.FileService
import org.gaziz.birgram.core.telegram.api.model.chat.ChatPhoto
import javax.inject.Inject

class DownloadChatPhotoSmall @Inject constructor(
    private val chatService: ChatService,
    private val fileService: FileService
) {
    operator fun invoke(
        chatId: Long,
        fileId: Int
    ) {
        fileService.downloadFile(
            fileId,
            onFile = { file ->
                chatService.updateChats { old ->
                    val chat = old[chatId] ?: return@updateChats old
                    var photo = ChatPhoto(
                        small = file,
                        miniThumbnail = null
                    )
                    if(chat.photo != null) {
                        photo = chat.photo.copy(small = file)
                    }
                    old + (chatId to chat.copy(photo = photo))
                }
            }
        )
    }
}