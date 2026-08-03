package org.gaziz.birgram.core.telegram.api.model.message

import org.gaziz.birgram.core.telegram.api.model.StickerFormat
import org.gaziz.birgram.core.telegram.api.model.media.FileData

sealed interface MessageContent {
    data class Text(val text: String): MessageContent
    data class Sticker(
        val emoji: String,
        val width: Int,
        val height: Int,
        val format: StickerFormat,
        val data: FileData
    ): MessageContent
    data class GIF(
        val miniThumbnail: ByteArray?,
        val caption: String
    ): MessageContent
    data class Photo(
        val miniThumbnail: ByteArray?,
        val caption: String
    ): MessageContent
    data class Video(
        val miniThumbnail: ByteArray?,
        val caption: String
    ): MessageContent
    data class Audio(val caption: String): MessageContent
    data class Document(
        val fileName: String,
        val caption: String
    ): MessageContent
    object VoiceNote: MessageContent
    data class VideoNote(val miniThumbnail: ByteArray?): MessageContent
    object Other: MessageContent
}