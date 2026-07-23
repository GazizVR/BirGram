package org.gaziz.birgram.features.chatList.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import org.gaziz.birgram.features.chatList.domain.model.ChatAvatar

data class PhotoUiState(
    val size: Dp,
    val photo: ChatAvatar,
    val overlay: @Composable () -> Unit = {}
)
