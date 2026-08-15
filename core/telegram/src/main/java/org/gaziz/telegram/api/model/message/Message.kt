package org.gaziz.telegram.api.model.message

import java.time.LocalDateTime

data class Message(
    val id: Long,
    val content: MessageContent,
    val date: LocalDateTime,
    val isOutgoing: Boolean,
    val chatId: Long,
    val sender: MessageSender
)