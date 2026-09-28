package org.gaziz.birgram.feature.chat.ui.model

import org.gaziz.birgram.core.ui.model.ChatTypeUiState
import org.gaziz.birgram.core.telegram.api.model.message.Message

data class ChatUiState(
    val id: Long,
    val title: String,
    val avatar: AvatarUiState,
    val isDeleted: Boolean,
    val typeInfo: ChatTypeUiState?,
    val draftText: String = "",
    val canSendTextMessages: Boolean = false,
    val unreadCount: Int? = null,
    val lastMessage: Message?
)