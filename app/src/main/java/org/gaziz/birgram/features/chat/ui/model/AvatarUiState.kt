package org.gaziz.birgram.features.chat.ui.model

import androidx.compose.ui.unit.Dp
import org.gaziz.birgram.core.telegram.api.model.media.Avatar

data class AvatarUiState(
    val avatar: Avatar?,
    val size: Dp
)
