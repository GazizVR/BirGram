package org.gaziz.birgram.features.chatList.domain.model

import org.gaziz.birgram.core.telegram.model.ChatPhoto
import org.gaziz.birgram.core.telegram.model.ChatType

data class ChatData(
    val id: Long,
    val title: String,
    val type: ChatType,
    val photo: ChatPhoto?,
    val lastMessage: LastMsgData?,
    val positions: List<ChatPosition>,
    val unreadCount: Int,
    val mentionCount: Int,
    val reactionCount: Int,
)