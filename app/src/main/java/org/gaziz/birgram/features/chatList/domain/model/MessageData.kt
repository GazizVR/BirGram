package org.gaziz.birgram.features.chatList.domain.model

import java.time.LocalDateTime

data class MessageData(
    val id: Long,
    val content: MessageContent,
    val date: LocalDateTime
)
