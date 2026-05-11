package org.gaziz.birgram.domain.model.chat

data class ChatPosition(
    val listType: ChatListType,
    val order: Long,
    val isPinned: Boolean
)

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