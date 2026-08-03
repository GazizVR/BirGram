package org.gaziz.birgram.features.chat.ui.model

import java.io.File

sealed interface StickerContent {
    data class Picture(val file: File): StickerContent
    data class Video(val path: String): StickerContent
    data class Animation(val path: String): StickerContent
    data class Empty(val downloadContent: () -> Unit): StickerContent
}

sealed interface MessageContentInfo {
    data class Text(val text: String):  MessageContentInfo
    data class Sticker(
        val emoji: String,
        val height: Int,
        val width: Int,
        val content: StickerContent
    ): MessageContentInfo
    object UnSupported: MessageContentInfo
}