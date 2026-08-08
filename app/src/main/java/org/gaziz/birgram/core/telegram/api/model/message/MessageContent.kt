package org.gaziz.birgram.core.telegram.api.model.message

import org.gaziz.birgram.core.telegram.api.model.StickerFormat
import org.gaziz.birgram.core.telegram.api.model.media.FileData
import org.gaziz.birgram.core.telegram.api.model.media.PhotoSize

sealed interface MessageContent {
    data class Text(val text: String): MessageContent
    data class Sticker(
        val emoji: String,
        val format: StickerFormat,
        val data: FileData
    ): MessageContent
    data class AnimatedEmoji(
        val emoji: String,
        val animation: Sticker?
    ): MessageContent
    data class GIF(
        val file: FileData,
        val caption: String,
        val width: Int,
        val height: Int,
        val miniThumbnail: ByteArray?
    ): MessageContent
    data class Photo(
        val miniThumbnail: ByteArray?,
        val sizes: List<PhotoSize>,
        val caption: String
    ): MessageContent
    data class Video(
        val miniThumbnail: ByteArray?,
        val caption: String
    ): MessageContent
    data class Audio(val caption: String): MessageContent
    data class Document(
        val file: FileData,
        val fileName: String,
        val mimeType: String,
        val caption: String
    ): MessageContent
    object VoiceNote: MessageContent
    data class VideoNote(val miniThumbnail: ByteArray?): MessageContent
    object Other: MessageContent
}