package org.gaziz.birgram.core.telegram.model

import java.time.LocalDateTime

data class DraftMessage(
    val content: DraftMessageContent,
    val date: LocalDateTime
)
