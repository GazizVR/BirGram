package org.gaziz.birgram.feature.chat.ui.model

import java.io.File

sealed interface MessageContentUiState {
    data class Text(val text: String):  MessageContentUiState
    data class Sticker(val content: StickerUiState): MessageContentUiState
    data class AnimatedEmoji(
        val emoji: String,
        val content: StickerUiState?
    ): MessageContentUiState
    data class Animation(
        val content: MediaUiState,
        val caption: String?,
        val width: Int,
        val height: Int
    ): MessageContentUiState
    data class Photo(
        val content: MediaUiState?,
        val caption: String?,
        val width: Int,
        val height: Int
    ): MessageContentUiState
    data class Document(
        val file: File?,
        val mimeType: String?,
        val downloadDocument: () -> Unit,
        val fileName: String?,
        val size: String?,
        val type: String?,
    ): MessageContentUiState
    data class Video(
        val content: MediaUiState?,
        val caption: String?,
        val width: Int,
        val height: Int,
        val duration: Int,
    ): MessageContentUiState
    object UnSupported: MessageContentUiState
}