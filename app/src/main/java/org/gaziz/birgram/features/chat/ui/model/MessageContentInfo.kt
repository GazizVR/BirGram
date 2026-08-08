package org.gaziz.birgram.features.chat.ui.model

import androidx.compose.ui.graphics.ImageBitmap
import java.io.File

sealed interface StickerContent {
    data class Picture(val file: File): StickerContent
    data class Video(val path: String): StickerContent
    data class Animation(val path: String): StickerContent
    data class Empty(
        val emoji: String,
        val downloadContent: () -> Unit
    ): StickerContent
}

sealed interface MediaContent {
    data class Image(
        val file: File,
        val isGIF: Boolean = false
    ): MediaContent
    data class PlaceHolder(val downloadMedia: () -> Unit): MediaContent
    data class Thumbnail(
        val data: ImageBitmap,
        val downloadMedia: () -> Unit
    ): MediaContent
}

sealed interface MessageContentInfo {
    data class Text(val text: String):  MessageContentInfo
    data class Sticker(val content: StickerContent): MessageContentInfo
    data class AnimatedEmoji(
        val emoji: String,
        val content: StickerContent?
    ): MessageContentInfo
    data class Animation(
        val content: MediaContent,
        val caption: String?,
        val width: Int,
        val height: Int
    ): MessageContentInfo
    data class Photo(
        val content: MediaContent?,
        val caption: String?,
        val width: Int,
        val height: Int
    ): MessageContentInfo
    object UnSupported: MessageContentInfo
}