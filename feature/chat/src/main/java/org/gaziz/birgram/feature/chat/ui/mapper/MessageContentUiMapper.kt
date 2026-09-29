package org.gaziz.birgram.feature.chat.ui.mapper

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import org.gaziz.birgram.core.telegram.api.model.message.MessageContent
import org.gaziz.birgram.feature.chat.ui.model.MediaUiState
import org.gaziz.birgram.feature.chat.ui.model.MessageContentUiState
import org.gaziz.birgram.feature.chat.ui.model.StickerUiState
import java.io.File
import java.util.Locale
import javax.inject.Inject
import kotlin.math.log10
import kotlin.math.pow

class MessageContentUiMapper @Inject constructor(
    private val stickerUiMapper: StickerUiMapper
) {
    private fun Long.toByteCount(): String? {
        if (this <= 0) return null

        val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB")
        val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()

        val index = digitGroups.coerceAtMost(units.lastIndex)
        val value = this / 1024.0.pow(index.toDouble())

        return if (index == 0) {
            "$this B"
        } else {
            String.format(Locale.US, "%.1f %s", value, units[index])
        }
    }
    private fun String.toFileType(): String {

        val mime = this.lowercase().trim()

        return when {
            mime == "application/pdf" -> "PDF"
            mime == "application/msword" -> "DOC"
            mime == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> "DOCX"
            mime == "application/vnd.ms-excel" -> "XLS"
            mime == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> "XLSX"
            mime == "application/vnd.ms-powerpoint" -> "PPT"
            mime == "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> "PPTX"
            mime == "text/plain" -> "TXT"
            mime == "text/html" -> "HTML"
            mime == "text/csv" -> "CSV"
            mime == "application/json" -> "JSON"
            mime == "application/zip" -> "ZIP"
            mime == "application/x-rar-compressed" || mime == "application/vnd.rar" -> "RAR"
            mime == "application/x-7z-compressed" -> "7Z"

            mime == "image/png" -> "PNG"
            mime == "image/jpeg" || mime == "image/jpg" -> "JPG"
            mime == "image/webp" -> "WEBP"
            mime == "image/gif" -> "GIF"
            mime == "image/svg+xml" -> "SVG"
            mime == "image/heic" || mime == "image/heif" -> "HEIC"

            mime == "audio/mpeg" || mime == "audio/mp3" -> "MP3"
            mime == "audio/wav" || mime == "audio/x-wav" -> "WAV"
            mime == "audio/ogg" -> "OGG"
            mime == "video/mp4" -> "MP4"
            mime == "video/x-matroska" -> "MKV"
            mime == "video/quicktime" -> "MOV"
            mime == "video/webm" -> "WEBM"

            mime.startsWith("image/") -> mime.substringAfter("image/").uppercase()
            mime.startsWith("video/") -> mime.substringAfter("video/").uppercase()
            mime.startsWith("audio/") -> mime.substringAfter("audio/").uppercase()
            mime.startsWith("text/") -> "TXT"

            else -> ""
        }
    }

    fun map(
        messageContent: MessageContent,
        downloadMedia: (Int) -> Unit
    ): MessageContentUiState {
        return when(messageContent) {
            is MessageContent.Text -> MessageContentUiState.Text(messageContent.text)
            is MessageContent.Sticker -> {
                val content = stickerUiMapper.map(messageContent,downloadMedia)
                MessageContentUiState.Sticker(content)
            }
            is MessageContent.AnimatedEmoji -> {
                var content: StickerUiState? = null
                val animation = messageContent.animation
                if(animation != null) {
                    content = stickerUiMapper.map(animation,downloadMedia)
                }
                MessageContentUiState.AnimatedEmoji(
                    emoji = messageContent.emoji,
                    content = content
                )
            }
            is MessageContent.Animation -> {
                val downloadAnimation = {
                    if(messageContent.file.canDownload) {
                        downloadMedia(messageContent.file.id)
                    }
                }
                var content: MediaUiState = MediaUiState.Placeholder(downloadAnimation)
                val miniThumbnail = messageContent.miniThumbnail
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
                if(messageContent.file.path.isNotBlank()) {
                    if(messageContent.mimeType == "image/gif") {
                        content = MediaUiState.Image(
                            File(messageContent.file.path),
                            true
                        )
                    }
                    if(messageContent.mimeType == "video/mp4") {
                        content = MediaUiState.Video(File(messageContent.file.path))
                    }
                }
                MessageContentUiState.Animation(
                    caption = messageContent.caption.ifBlank { null },
                    content = content,
                    width = messageContent.width,
                    height = messageContent.height
                )
            }
            is MessageContent.Document -> {
                MessageContentUiState.Document(
                    file = if(messageContent.file.path.isNotBlank()) File(messageContent.file.path) else null,
                    mimeType = messageContent.mimeType.ifBlank { null },
                    fileName = messageContent.fileName.ifBlank { null },
                    size = messageContent.file.size.toByteCount(),
                    type = messageContent.mimeType.toFileType(),
                    downloadDocument = {
                        if(messageContent.file.canDownload) {
                            downloadMedia(messageContent.file.id)
                        }
                    }
                )
            }
            is MessageContent.Video -> {
                val content: MediaUiState = when {
                    messageContent.file.path.isNotBlank() -> {
                        MediaUiState.Video(
                            File(messageContent.file.path)
                        )
                    }
                    messageContent.miniThumbnail != null -> {
                        val bitmap = BitmapFactory.decodeByteArray(
                            messageContent.miniThumbnail,
                            0,
                            messageContent.miniThumbnail?.size ?: 0
                        ).asImageBitmap()
                        MediaUiState.Thumbnail(
                            data = bitmap,
                            onDownloadClick = { downloadMedia(messageContent.file.id) }
                        )
                    }
                    else -> MediaUiState.Placeholder { downloadMedia(messageContent.file.id) }
                }
                MessageContentUiState.Video(
                    content = content,
                    caption = messageContent.caption.ifBlank { null },
                    width = messageContent.width,
                    height = messageContent.height,
                    duration = messageContent.duration
                )
            }
            else -> MessageContentUiState.UnSupported
        }
    }
}