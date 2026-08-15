package org.gaziz.telegram.api.model.chat

data class ChatPosition(
    val listType: ChatListType,
    val order: Long,
    val isPinned: Boolean
)