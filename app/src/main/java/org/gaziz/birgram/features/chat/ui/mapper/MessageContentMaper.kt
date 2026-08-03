package org.gaziz.birgram.features.chat.ui.mapper

import org.gaziz.birgram.core.telegram.api.model.StickerFormat
import org.gaziz.birgram.core.telegram.api.model.message.MessageContent
import org.gaziz.birgram.features.chat.ui.model.MessageContentInfo
import org.gaziz.birgram.features.chat.ui.model.StickerContent
import java.io.File

fun MessageContent.toInfo(
    downloadMedia: (Int) -> Unit
): MessageContentInfo {
    return when(this) {
        is MessageContent.Sticker -> {
            val content = if(this.data.path.isNotBlank()) {
                when(this.format){
                    StickerFormat.Tgs -> StickerContent.Animation(this.data.path)
                    StickerFormat.WebM -> StickerContent.Video(this.data.path)
                    StickerFormat.WebP -> StickerContent.Picture(File(this.data.path))
                }
            } else {
                StickerContent.Empty {
                    if(this.data.canDownload) {
                        downloadMedia(this.data.id)
                    }
                }
            }
            MessageContentInfo.Sticker(
                emoji = this.emoji,
                content = content
            )
        }
        is MessageContent.Text -> MessageContentInfo.Text(this.text)
        else -> MessageContentInfo.UnSupported
    }
}