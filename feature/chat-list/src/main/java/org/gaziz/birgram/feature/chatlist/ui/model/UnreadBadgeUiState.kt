package org.gaziz.birgram.feature.chatlist.ui.model

import androidx.compose.ui.unit.TextUnit

data class UnreadBadgeUiState(
    val unreadCount: Int,
    val mentionCount: Int,
    val reactionCount: Int,

    val fontSize: TextUnit
)
