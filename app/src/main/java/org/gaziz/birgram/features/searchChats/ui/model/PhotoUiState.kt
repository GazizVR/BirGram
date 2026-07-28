package org.gaziz.birgram.features.searchChats.ui.model

import androidx.compose.ui.unit.Dp
import org.gaziz.birgram.core.telegram.api.model.Avatar

data class PhotoUiState(
    val avatar: Avatar,
    val size: Dp
)
