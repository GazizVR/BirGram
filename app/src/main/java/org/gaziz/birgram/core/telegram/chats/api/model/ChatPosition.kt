package org.gaziz.birgram.core.telegram.chats.api.model

data class ChatPosition(
    val listType: ChatListType,
    val order: Long,
    val isPinned: Boolean
)