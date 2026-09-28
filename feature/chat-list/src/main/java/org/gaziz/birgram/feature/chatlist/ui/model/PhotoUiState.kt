package org.gaziz.birgram.feature.chatlist.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import org.gaziz.birgram.core.ui.model.AvatarUiState

data class PhotoUiState(
    val size: Dp,
    val photo: AvatarUiState,
    val overlay: @Composable () -> Unit = {}
)
