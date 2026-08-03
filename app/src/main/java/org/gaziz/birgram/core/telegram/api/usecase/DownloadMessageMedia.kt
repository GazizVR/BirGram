package org.gaziz.birgram.core.telegram.api.usecase

import org.gaziz.birgram.core.telegram.api.MessageService
import org.gaziz.birgram.core.telegram.api.model.message.MessageContent
import javax.inject.Inject

class DownloadMessageMedia @Inject constructor(
    private val downloadOrGetFileDataById: DownloadOrGetFileDataById,
    private val messageService: MessageService
) {
    operator fun invoke(
        fileId: Int,
        messageId: Long
    ) {
        downloadOrGetFileDataById(
            fileId = fileId,
            onFile = { file ->
                messageService.updateMessages { old ->
                    val msg = old[messageId] ?: return@updateMessages old
                    val content: MessageContent = when(msg.content) {
                        is MessageContent.Sticker -> msg.content.copy(data = file)
                        else -> return@updateMessages old
                    }
                    val newMsg = msg.copy(content = content)
                    old + (messageId to newMsg)
                }
            }
        )
    }
}