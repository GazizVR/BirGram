package org.gaziz.birgram.feature.chat.ui.model

import java.io.File

sealed interface StickerUiState {
    data class Picture(val file: File): StickerUiState
    data class Video(val file: File): StickerUiState
    data class Animation(val path: String): StickerUiState
    data class Empty(
        val emoji: String,
        val onDownloadClick: () -> Unit
    ): StickerUiState
}
