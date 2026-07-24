package org.gaziz.birgram.features.chatList.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import org.gaziz.birgram.core.ui.model.ChatAvatar

data class PhotoUiState(
    val size: Dp,
    val photo: ChatAvatar,
    val overlay: @Composable () -> Unit = {}
)
