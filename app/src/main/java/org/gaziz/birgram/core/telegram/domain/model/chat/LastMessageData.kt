package org.gaziz.birgram.core.telegram.domain.model.chat

import org.gaziz.birgram.core.telegram.domain.model.message.MessageContent
import java.time.LocalDateTime

data class LastMessageData(
    val id: Long,
    val content: MessageContent,
    val date: LocalDateTime
)
