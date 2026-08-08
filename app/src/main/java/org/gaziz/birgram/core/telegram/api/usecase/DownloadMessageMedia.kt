package org.gaziz.birgram.core.telegram.api.usecase

import org.gaziz.birgram.core.telegram.api.MessageService
import org.gaziz.birgram.core.telegram.api.model.media.FileData
import org.gaziz.birgram.core.telegram.api.model.message.Message
import org.gaziz.birgram.core.telegram.api.model.message.MessageContent
import javax.inject.Inject

class DownloadMessageMedia @Inject constructor(
    private val downloadOrGetFileDataById: DownloadOrGetFileDataById,
    private val messageService: MessageService
) {
    operator fun invoke(
        fileId: Int,
        messageId: Long,
        onFile: ((FileData,Message) -> Message)? = null
    ) {
        downloadOrGetFileDataById(
            fileId = fileId,
            onFile = { file ->
                messageService.updateMessages { old ->
                    val msg = old[messageId] ?: return@updateMessages old
                    if(onFile != null) {
                        val newMsg = onFile(file,msg)
                        old + (messageId to newMsg)
                    } else {
                        val content: MessageContent = when(msg.content) {
                            is MessageContent.Sticker -> msg.content.copy(data = file)
                            is MessageContent.AnimatedEmoji -> {
                                var sticker: MessageContent.Sticker? = null
                                if(msg.content.animation != null) {
                                    sticker =  msg.content.animation.copy(data = file)
                                }
                                msg.content.copy(animation = sticker)
                            }
                            is MessageContent.Animation -> {
                                msg.content.copy(file = file)
                            }
                            is MessageContent.Document -> {
                                msg.content.copy(file = file)
                            }
                            else -> return@updateMessages old
                        }
                        val newMsg = msg.copy(content = content)
                        old + (messageId to newMsg)
                    }
                }
            }
        )
    }
}