package org.gaziz.birgram.features.chat.ui.model

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

sealed interface MessageContentInfo {
    data class Text(val text: String):  MessageContentInfo
    data class Sticker(val content: StickerContent): MessageContentInfo
    data class AnimatedEmoji(
        val emoji: String,
        val content: StickerContent?
    ): MessageContentInfo
    object UnSupported: MessageContentInfo
}