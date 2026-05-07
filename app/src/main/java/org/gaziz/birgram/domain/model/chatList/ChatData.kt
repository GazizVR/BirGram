package org.gaziz.birgram.domain.model.chatList

import org.gaziz.birgram.domain.model.FileData

data class ChatPosition(
    val listType: ChatListType,
    val order: Long,
    val isPinned: Boolean
)

data class ChatPhoto(
    val miniThumbnail: ByteArray?,
    val small: FileData
)

data class ChatData(
    val id: Long,
    val title: String,
    val photo: ChatPhoto?,
    val lastMessage: LastMessageData?,
    val positions: List<ChatPosition>,
    val unreadCount: Int,
    val mentionCount: Int,
    val reactionCount: Int
)