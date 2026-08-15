package org.gaziz.telegram.api.model

sealed interface StickerFormat {
    object Tgs: StickerFormat
    object WebM: StickerFormat
    object WebP: StickerFormat
}