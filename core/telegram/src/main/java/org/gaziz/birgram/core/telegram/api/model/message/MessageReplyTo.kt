package org.gaziz.birgram.core.telegram.api.model.message

sealed interface MessageReplyTo {
    data class Message(
        val id: Long,
        val chatId: Long,
        val quote: String?,
    ): MessageReplyTo
}