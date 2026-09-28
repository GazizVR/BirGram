package org.gaziz.birgram.core.telegram.ui.model

import androidx.compose.ui.graphics.Color

data class MessageSenderUiState(
    val name: String? = null,
    val avatar: AvatarUiState? = null,
    val accentColor: Color
)