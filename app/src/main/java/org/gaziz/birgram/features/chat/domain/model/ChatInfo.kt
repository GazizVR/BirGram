package org.gaziz.birgram.features.chat.domain.model

import org.gaziz.birgram.core.ui.model.ChatAvatar

data class ChatInfo(
    val title: String,
    val avatar: ChatAvatar,
    val isDeleted: Boolean
)
