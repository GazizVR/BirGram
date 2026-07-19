package org.gaziz.birgram.core.telegram.api.model.chat

import org.gaziz.birgram.core.telegram.api.model.message.DraftMessage
import org.gaziz.birgram.core.telegram.api.model.message.Message

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