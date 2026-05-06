package org.gaziz.birgram.domain.model.chatList

import java.time.LocalDateTime

sealed class MessageContent {
    data class Text(val text: String): MessageContent()
    object Photo: MessageContent()
    object Video: MessageContent()
    object Audio: MessageContent()
    object Document: MessageContent()

    object Sticker: MessageContent()
    object VoiceNote: MessageContent()
    object VideoNote: MessageContent()
    object GIF: MessageContent()
    object Call: MessageContent()

    data class Other(val type: String): MessageContent()
}

data class MessageData(
    val id: Long,
    val content: MessageContent,
    val date: LocalDateTime
)
