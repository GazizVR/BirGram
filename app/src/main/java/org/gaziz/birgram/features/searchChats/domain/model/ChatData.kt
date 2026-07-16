package org.gaziz.birgram.features.searchChats.domain.model

import org.gaziz.birgram.core.telegram.model.ChatPhoto
import org.gaziz.birgram.core.telegram.model.ChatType

data class ChatData(
    val id: Long,
    val title: String,
    val photo: ChatPhoto?,
    val type: ChatType
)
