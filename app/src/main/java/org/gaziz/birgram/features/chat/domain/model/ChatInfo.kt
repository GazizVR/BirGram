package org.gaziz.birgram.features.chat.domain.model

import org.gaziz.birgram.core.ui.model.ChatAvatar
import org.gaziz.birgram.core.ui.model.ChatTypeInfo

data class ChatInfo(
    val title: String,
    val avatar: ChatAvatar,
    val isDeleted: Boolean,
    val typeInfo: ChatTypeInfo?
)
