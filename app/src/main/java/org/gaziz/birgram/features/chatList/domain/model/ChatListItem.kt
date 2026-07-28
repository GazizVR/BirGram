package org.gaziz.birgram.features.chatList.domain.model

import org.gaziz.birgram.core.telegram.api.model.message.DraftMessage
import org.gaziz.birgram.core.telegram.api.model.message.Message
import org.gaziz.birgram.core.telegram.api.model.Avatar

data class ChatListItem(
    val id: Long,
    val title: String,
    val lastMessage: Message?,
    val draftMessage: DraftMessage?,
    val unreadCount: Int,
    val mentionCount: Int,
    val reactionCount: Int,

    val isDeleted: Boolean,
    val lastMsgDate: String,
    val avatar: Avatar,
    val isDraftMsg: Boolean,
    val isOnline: Boolean,
    val messageSender: String?
)