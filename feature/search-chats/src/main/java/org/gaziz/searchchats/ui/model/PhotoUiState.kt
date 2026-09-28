package org.gaziz.searchchats.ui.model

import androidx.compose.ui.unit.Dp
import org.gaziz.birgram.core.telegram.ui.model.AvatarUiState

data class PhotoUiState(
    val avatar: AvatarUiState,
    val size: Dp
)
