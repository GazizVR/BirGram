package org.gaziz.birgram.features.chatList.domain.model

import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.ui.model.ChatAvatar

data class ChatListItem(
    val chat: Chat,
    val isDeleted: Boolean,
    val lastMsgDate: String,
    val avatar: ChatAvatar,
    val isDraftMsg: Boolean,
    val isOnline: Boolean,
    val messageSender: String?
)