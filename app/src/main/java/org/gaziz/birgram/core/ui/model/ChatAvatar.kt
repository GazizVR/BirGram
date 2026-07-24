package org.gaziz.birgram.core.ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface ChatAvatar {
    data class Photo(
        val bitmap: ImageBitmap,
        val onEmpty: () -> Unit = {}
    ): ChatAvatar
    data class Icon(
        val imageVector: ImageVector,
        val background: Color
    ): ChatAvatar
    data class PlaceHolder(
        val text: String,
        val color: Color,
        val downloadPhoto: () -> Unit = {}
    ): ChatAvatar
}