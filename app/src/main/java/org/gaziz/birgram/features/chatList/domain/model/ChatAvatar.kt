package org.gaziz.birgram.features.chatList.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface ChatAvatar {
    data class Photo(val bitmap: ImageBitmap): ChatAvatar
    data class Icon(
        val imageVector: ImageVector,
        val background: Color
    ): ChatAvatar
    data class PlaceHolder(
        val text: String,
        val color: Color,
        val callback: () -> Unit = {}
    ): ChatAvatar
}