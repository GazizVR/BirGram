package org.gaziz.birgram.domain.model.chat

import org.gaziz.birgram.domain.model.message.MessageContent

data class LastMessageData(
    val id: Long,
    val content: MessageContent,
    val date: String
)
