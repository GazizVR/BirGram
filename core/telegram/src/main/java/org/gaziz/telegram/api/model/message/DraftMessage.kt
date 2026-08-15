package org.gaziz.telegram.api.model.message

import java.time.LocalDateTime

data class DraftMessage(
    val content: DraftMessageContent,
    val date: LocalDateTime
)
