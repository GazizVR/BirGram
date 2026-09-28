package org.gaziz.birgram.feature.chat.ui.model

import androidx.compose.ui.unit.Dp
import org.gaziz.birgram.core.telegram.ui.model.AvatarUiState

data class ChatAvatarUiState(
    val avatar: AvatarUiState?,
    val size: Dp
)
