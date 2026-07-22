package org.gaziz.birgram.core.telegram.api.model.message

sealed interface MessageContent {
    data class Text(val text: String): MessageContent
    data class Sticker(val emoji: String): MessageContent
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
    object Other: MessageContent
}