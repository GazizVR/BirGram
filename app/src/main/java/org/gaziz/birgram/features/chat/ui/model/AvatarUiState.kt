package org.gaziz.birgram.features.chat.ui.model

import androidx.compose.ui.unit.Dp
import org.gaziz.birgram.core.ui.model.ChatAvatar

data class AvatarUiState(
    val avatar: ChatAvatar?,
    val size: Dp
)
