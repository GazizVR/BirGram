package org.gaziz.birgram.features.chat.domain.model

import org.gaziz.birgram.core.telegram.domain.model.chat.ChatPhoto
import org.gaziz.birgram.core.telegram.domain.model.chat.ChatType

data class ChatData(
    val id: Long,
    val title: String,
    val type: ChatType,
    val photo: ChatPhoto?,
    val canSendBasicMsg: Boolean
)