package org.gaziz.birgram.features.chatList.domain.model.chat

import org.gaziz.birgram.features.chatList.domain.model.MessageContent
import java.time.LocalDateTime

data class MessageData(
    val id: Long,
    val content: MessageContent,
    val date: LocalDateTime
)
