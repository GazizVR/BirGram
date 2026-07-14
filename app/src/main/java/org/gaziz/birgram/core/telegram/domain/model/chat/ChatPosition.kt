package org.gaziz.birgram.core.telegram.domain.model.chat

data class ChatPosition(
    val listType: ChatListType,
    val order: Long,
    val isPinned: Boolean
)