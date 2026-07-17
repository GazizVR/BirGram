package org.gaziz.birgram.features.chat.domain.model

import org.gaziz.birgram.core.telegram.model.ChatPhoto
import org.gaziz.birgram.core.telegram.model.ChatType
import org.gaziz.birgram.core.telegram.model.DraftMessage

data class ChatData(
    val id: Long,
    val title: String,
    val type: ChatType,
    val photo: ChatPhoto?,
    val canSendBasicMsg: Boolean,
    val draftMessage: DraftMessage?
)