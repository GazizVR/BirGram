package org.gaziz.birgram.features.chatList.ui.model

import androidx.compose.ui.unit.TextUnit

data class CardUnreadBadge(
    val unreadCount: Int,
    val mentionCount: Int,
    val reactionCount: Int,

    val fontSize: TextUnit
)
