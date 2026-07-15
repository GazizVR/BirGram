package org.gaziz.birgram.features.chatList.domain.model

data class ChatPosition(
    val listType: ChatListType,
    val order: Long,
    val isPinned: Boolean
)