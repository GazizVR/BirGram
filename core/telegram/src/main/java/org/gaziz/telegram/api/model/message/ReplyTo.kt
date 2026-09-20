package org.gaziz.telegram.api.model.message

sealed interface ReplyTo {
    data class Message(
        val id: Long,
        val chatId: Long,
        val quote: String?,
    ): ReplyTo
}