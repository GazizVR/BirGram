package org.gaziz.birgram.core.telegram.domain.model.chat

data class ChatData(
    val id: Long,
    val title: String,
    val type: ChatType,
    val photo: ChatPhoto?,
    val lastMessage: LastMessageData?,
    val positions: List<ChatPosition>,
    val unreadCount: Int,
    val mentionCount: Int,
    val reactionCount: Int,
    val canSendBasicMsg: Boolean
)