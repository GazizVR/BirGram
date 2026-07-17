package org.gaziz.birgram.features.chatList.domain.model

import java.time.LocalDateTime

data class LastMsgData(
    val id: Long,
    val content: LastMsgContent,
    val date: LocalDateTime
)
