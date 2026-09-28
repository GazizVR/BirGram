package org.gaziz.birgram.feature.chat.ui.model

import androidx.compose.ui.graphics.ImageBitmap
import java.io.File

sealed interface MediaUiState {
    data class Image(
        val file: File,
        val isGIF: Boolean = false
    ): MediaUiState
    data class Video(
        val file: File
    ): MediaUiState
    data class Placeholder(val onDownloadClick: () -> Unit): MediaUiState
    data class Thumbnail(
        val data: ImageBitmap,
        val onDownloadClick: () -> Unit
    ): MediaUiState
}
