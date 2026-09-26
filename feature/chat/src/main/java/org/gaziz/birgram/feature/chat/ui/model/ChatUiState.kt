package org.gaziz.birgram.feature.chat.ui.model

import org.gaziz.birgram.core.ui.model.Avatar
import org.gaziz.birgram.core.ui.model.ChatTypeInfo
import org.gaziz.birgram.core.telegram.api.model.message.Message

data class ChatUiState(
    val id: Long,
    val title: String,
    val avatar: Avatar,
    val isDeleted: Boolean,
    val typeInfo: ChatTypeInfo?,
    val draftText: String = "",
    val canSendTextMessages: Boolean = false,
    val unreadCount: Int? = null,
    val lastMessage: Message?
)