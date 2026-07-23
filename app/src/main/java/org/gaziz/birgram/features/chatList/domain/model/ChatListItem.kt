package org.gaziz.birgram.features.chatList.domain.model

import androidx.compose.ui.graphics.Color
import org.gaziz.birgram.core.telegram.api.model.chat.Chat

data class ChatListItem(
    val chat: Chat,
    val isDeleted: Boolean,
    val lastMsgDate: String,
    val accentColor: Color,
    val isDraftMsg: Boolean,
    val isOnline: Boolean,
    val messageSender: String?
)