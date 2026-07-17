package org.gaziz.birgram.features.chatList.domain.model

sealed interface LastMsgContent {
    data class Text(val text: String): LastMsgContent
    data class Document(
        val caption: String,
        val fileName: String
    ): LastMsgContent
    data class Audio(
        val caption: String,
        val fileName: String
    ): LastMsgContent
    data class Sticker(val emoji: String): LastMsgContent
    object VoiceNote: LastMsgContent
    data class Video(
        val caption: String,
        val fileName: String
    ): LastMsgContent
    object VideoNote: LastMsgContent

    data class Photo(
        val caption: String,
        val miniThumbNail: ByteArray?
    ): LastMsgContent

    object GIF: LastMsgContent

    object Other: LastMsgContent
}