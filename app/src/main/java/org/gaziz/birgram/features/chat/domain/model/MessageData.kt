package org.gaziz.birgram.features.chat.domain.model

import java.time.LocalDateTime

data class MessageData(
    val id: Long,
    val content: MessageContent,
    val date: LocalDateTime,
    val isMy: Boolean,
    val chatId: Long
)