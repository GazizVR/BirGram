package org.gaziz.birgram.core.ui.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface Avatar {
    data class Photo(
        val bitmap: ImageBitmap,
        val onEmpty: () -> Unit = {}
    ): Avatar
    data class Icon(
        val imageVector: ImageVector,
        val background: Color
    ): Avatar
    data class PlaceHolder(
        val text: String,
        val color: Color,
        val downloadPhoto: () -> Unit = {}
    ): Avatar
}