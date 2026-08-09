package org.gaziz.birgram.features.chat.ui.mapper

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import org.gaziz.birgram.core.telegram.api.model.StickerFormat
import org.gaziz.birgram.core.telegram.api.model.message.MessageContent
import org.gaziz.birgram.features.chat.ui.model.MediaContent
import org.gaziz.birgram.features.chat.ui.model.MessageContentInfo
import org.gaziz.birgram.features.chat.ui.model.StickerContent
import java.io.File

fun MessageContent.Sticker.toCnt(
    downloadMedia: (Int) -> Unit
): StickerContent {
    return if(this.data.path.isNotBlank()) {
        when(this.format){
            StickerFormat.Tgs -> StickerContent.Animation(this.data.path)
            StickerFormat.WebM -> StickerContent.Video(this.data.path)
            StickerFormat.WebP -> StickerContent.Picture(File(this.data.path))
        }
    } else {
        StickerContent.Empty(
            this.emoji
        ) {
            if(this.data.canDownload) {
                downloadMedia(this.data.id)
            }
        }
    }
}

fun MessageContent.toInfo(
    downloadMedia: (Int) -> Unit
): MessageContentInfo {
    return when(this) {
        is MessageContent.Text -> MessageContentInfo.Text(this.text)
        is MessageContent.Sticker -> {
            MessageContentInfo.Sticker(content = this.toCnt(downloadMedia))
        }
        is MessageContent.AnimatedEmoji -> {
            var content: StickerContent? = null
            if(this.animation != null) {
                content = this.animation.toCnt(downloadMedia)
            }
            MessageContentInfo.AnimatedEmoji(
                emoji = this.emoji,
                content = content
            )
        }
        is MessageContent.Animation -> {
            val downloadAnimation = {
                if(this.file.canDownload) {
                    downloadMedia(this.file.id)
                }
            }
            var content: MediaContent = MediaContent.PlaceHolder(downloadAnimation)
            if(this.miniThumbnail != null) {
                val bitmap = BitmapFactory.decodeByteArray(
                    this.miniThumbnail,
                    0,
                    this.miniThumbnail.size
                ).asImageBitmap()
                content = MediaContent.Thumbnail(
                    data = bitmap,
                    downloadMedia = downloadAnimation
                )
            }
            if(this.file.path.isNotBlank()) {
                if(this.mimeType == "image/gif") {
                    content = MediaContent.Image(
                        File(this.file.path),
                        true
                    )
                }
            }
            MessageContentInfo.Animation(
                caption = this.caption.ifBlank { null },
                content = content,
                width = this.width,
                height = this.height
            )
        }
        is MessageContent.Document -> {
            MessageContentInfo.Document(
                file = if(this.file.path.isNotBlank()) File(this.file.path) else null,
                mimeType = this.mimeType.ifBlank { null },
                fileName = this.fileName.ifBlank { null },
                size = this.file.size.toByteCount(),
                type = this.mimeType.toFileType(),
                downloadDocument = {
                    if(this.file.canDownload) {
                        downloadMedia(this.file.id)
                    }
                }
            )
        }
        else -> MessageContentInfo.UnSupported
    }
}