package org.gaziz.birgram.feature.chat.ui.mapper

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import org.gaziz.birgram.feature.chat.ui.model.MediaUiState
import org.gaziz.birgram.feature.chat.ui.model.MessageContentUiState
import org.gaziz.birgram.feature.chat.ui.model.StickerUiState
import org.gaziz.birgram.core.telegram.api.model.StickerFormat
import org.gaziz.birgram.core.telegram.api.model.message.MessageContent
import java.io.File

fun MessageContent.Sticker.toCnt(
    downloadMedia: (Int) -> Unit
): StickerUiState {
    return if(this.data.path.isNotBlank()) {
        when(this.format){
            StickerFormat.Tgs -> StickerUiState.Animation(this.data.path)
            StickerFormat.WebM -> StickerUiState.Video(File(this.data.path))
            StickerFormat.WebP -> StickerUiState.Picture(File(this.data.path))
        }
    } else {
        StickerUiState.Empty(
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
): MessageContentUiState {
    return when(this) {
        is MessageContent.Text -> MessageContentUiState.Text(this.text)
        is MessageContent.Sticker -> {
            MessageContentUiState.Sticker(content = this.toCnt(downloadMedia))
        }
        is MessageContent.AnimatedEmoji -> {
            var content: StickerUiState? = null
            val animation = this.animation
            if(animation != null) {
                content = animation.toCnt(downloadMedia)
            }
            MessageContentUiState.AnimatedEmoji(
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
            var content: MediaUiState = MediaUiState.Placeholder(downloadAnimation)
            val miniThumbnail = this.miniThumbnail
            if(miniThumbnail != null) {
                val bitmap = BitmapFactory.decodeByteArray(
                    miniThumbnail,
                    0,
                    miniThumbnail.size
                ).asImageBitmap()
                content = MediaUiState.Thumbnail(
                    data = bitmap,
                    onDownloadClick = downloadAnimation
                )
            }
            if(this.file.path.isNotBlank()) {
                if(this.mimeType == "image/gif") {
                    content = MediaUiState.Image(
                        File(this.file.path),
                        true
                    )
                }
                if(this.mimeType == "video/mp4") {
                    content = MediaUiState.Video(File(this.file.path))
                }
            }
            MessageContentUiState.Animation(
                caption = this.caption.ifBlank { null },
                content = content,
                width = this.width,
                height = this.height
            )
        }
        is MessageContent.Document -> {
            MessageContentUiState.Document(
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
        is MessageContent.Video -> {
            val content: MediaUiState = when {
                this.file.path.isNotBlank() -> {
                    MediaUiState.Video(
                        File(this.file.path)
                    )
                }
                this.miniThumbnail != null -> {
                    val bitmap = BitmapFactory.decodeByteArray(
                        this.miniThumbnail,
                        0,
                        this.miniThumbnail?.size ?: 0
                    ).asImageBitmap()
                    MediaUiState.Thumbnail(
                        data = bitmap,
                        onDownloadClick = { downloadMedia(this.file.id) }
                    )
                }
                else -> MediaUiState.Placeholder { downloadMedia(this.file.id) }
            }
            MessageContentUiState.Video(
                content = content,
                caption = this.caption.ifBlank { null },
                width = this.width,
                height = this.height,
                duration = this.duration
            )
        }
        else -> MessageContentUiState.UnSupported
    }
}