package org.gaziz.birgram.core.telegram.api.model.chat

import java.time.LocalDateTime

data class DraftMessage(
    val content: DraftMessageContent,
    val date: LocalDateTime
)
