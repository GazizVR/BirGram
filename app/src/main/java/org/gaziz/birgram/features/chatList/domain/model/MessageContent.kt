package org.gaziz.birgram.features.chatList.domain.model

sealed interface MessageContent {
    data class Text(val text: String): MessageContent
    data class Document(
        val caption: String,
        val fileName: String
    ): MessageContent
    data class Audio(
        val caption: String,
        val fileName: String
    ): MessageContent
    data class Sticker(val emoji: String): MessageContent
    object VoiceNote: MessageContent
    data class Video(
        val caption: String,
        val fileName: String
    ): MessageContent
    object VideoNote: MessageContent

    data class Photo(
        val caption: String,
        val miniThumbNail: ByteArray?
    ): MessageContent

    object GIF: MessageContent

    object Other: MessageContent
}