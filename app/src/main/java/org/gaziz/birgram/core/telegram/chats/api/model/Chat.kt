package org.gaziz.birgram.core.telegram.chats.api.model

import org.gaziz.birgram.core.telegram.messages.api.model.Message

data class Chat(
    val id: Long,
    val title: String,
    val type: ChatType,
    val photo: ChatPhoto?,
    val lastMessage: Message?,
    val draftMessage: DraftMessage?,
    val positions: List<ChatPosition>,
    val unreadCount: Int,
    val mentionCount: Int,
    val reactionCount: Int,
)