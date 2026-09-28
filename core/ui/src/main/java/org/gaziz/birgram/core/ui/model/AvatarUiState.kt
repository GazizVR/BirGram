package org.gaziz.birgram.core.ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface AvatarUiState {
    data class Photo(
        val bitmap: ImageBitmap,
        val onEmpty: () -> Unit = {}
    ): AvatarUiState
    data class Icon(
        val imageVector: ImageVector,
        val background: Color
    ): AvatarUiState
    data class PlaceHolder(
        val text: String,
        val color: Color,
        val downloadPhoto: () -> Unit = {}
    ): AvatarUiState
}