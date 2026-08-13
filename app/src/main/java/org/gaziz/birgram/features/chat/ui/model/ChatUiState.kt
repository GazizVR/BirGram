package org.gaziz.birgram.features.chat.ui.model

import org.gaziz.birgram.core.ui.model.Avatar
import org.gaziz.birgram.core.ui.model.ChatTypeInfo

data class ChatUiState(
    val id: Long,
    val title: String,
    val avatar: Avatar,
    val isDeleted: Boolean,
    val typeInfo: ChatTypeInfo?,
    val draftText: String = "",
    val canSendTextMessages: Boolean = false,
)