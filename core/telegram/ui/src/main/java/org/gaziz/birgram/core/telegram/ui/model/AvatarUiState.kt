package org.gaziz.birgram.core.telegram.ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface AvatarUiState {
    data class Photo(val path: String): AvatarUiState
    data class Thumbnail(
        val bitmap: ImageBitmap,
        val onDownload: () -> Unit = {}
    ): AvatarUiState
    data class PlaceHolder(
        val text: String,
        val color: Color,
        val onDownload: () -> Unit = {}
    ): AvatarUiState
    data class Icon(
        val imageVector: ImageVector,
        val background: Color
    ): AvatarUiState
}