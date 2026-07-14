package org.gaziz.birgram.core.telegram.domain.model.chat

import org.gaziz.birgram.core.telegram.domain.model.message.MessageContent

data class LastMessageData(
    val id: Long,
    val content: MessageContent,
    val date: String
)
