package org.gaziz.birgram.core.telegram.api.model

sealed interface StickerFormat {
    object Tgs: StickerFormat
    object WebM: StickerFormat
    object WebP: StickerFormat
}