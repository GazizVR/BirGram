package org.gaziz.birgram.core.telegram.messages.api.model

import java.time.LocalDateTime

data class Message(
    val id: Long,
    val content: MessageContent,
    val date: LocalDateTime,
    val isMy: Boolean,
    val chatId: Long
)