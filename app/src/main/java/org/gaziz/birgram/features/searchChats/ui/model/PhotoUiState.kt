package org.gaziz.birgram.features.searchChats.ui.model

import androidx.compose.ui.unit.Dp
import org.gaziz.birgram.core.ui.model.ChatAvatar

data class PhotoUiState(
    val avatar: ChatAvatar,
    val size: Dp
)
