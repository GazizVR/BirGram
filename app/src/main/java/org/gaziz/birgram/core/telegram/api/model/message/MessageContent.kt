package org.gaziz.birgram.core.telegram.api.model.message

sealed interface MessageContent {
    data class Text(val text: String): MessageContent
    data class Sticker(val emoji: String): MessageContent
    object Other: MessageContent
}