package org.gaziz.birgram.domain.model.chatList

data class ChatPosition(
    val list: ChatListType,
    val order: Long,
    val isPinned: Boolean
)