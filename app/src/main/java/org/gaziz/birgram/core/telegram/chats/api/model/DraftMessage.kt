package org.gaziz.birgram.core.telegram.chats.api.model

import java.time.LocalDateTime

data class DraftMessage(
    val content: DraftMessageContent,
    val date: LocalDateTime
)
