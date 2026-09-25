package org.gaziz.telegram.api.model.message

sealed interface MessageReplyTo {
    data class Message(
        val id: Long,
        val chatId: Long,
        val quote: String?,
    ): MessageReplyTo
}