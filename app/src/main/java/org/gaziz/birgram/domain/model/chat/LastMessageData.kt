package org.gaziz.birgram.domain.model.chat

sealed class LastMessageContent {
    data class Text(val text: String): LastMessageContent()
    data class Document(
        val caption: String,
        val fileName: String
    ): LastMessageContent()
    data class Audio(
        val caption: String,
        val fileName: String
    ): LastMessageContent()
    data class Sticker(val emoji: String): LastMessageContent()
    object VoiceNote: LastMessageContent()
    data class Video(
        val caption: String,
        val fileName: String
    ): LastMessageContent()
    object VideoNote: LastMessageContent()

    data class Photo(
        val caption: String,
        val miniThumbNail: ByteArray?
    ): LastMessageContent()

    object GIF: LastMessageContent()

    data class Other(val type: String): LastMessageContent()
}

data class LastMessageData(
    val id: Long,
    val content: LastMessageContent,
    val date: String
)
