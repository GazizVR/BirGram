package org.gaziz.birgram.features.chatList.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import org.gaziz.birgram.core.telegram.api.model.media.Avatar

data class PhotoUiState(
    val size: Dp,
    val photo: Avatar,
    val overlay: @Composable () -> Unit = {}
)
