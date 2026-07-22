package org.gaziz.birgram.features.chatList.ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class PhotoUiState(
    val model: Any?,
    val size: Dp,
    val placeHolderColor: Color,
    val onNull: (Int) -> Unit = {},
)
