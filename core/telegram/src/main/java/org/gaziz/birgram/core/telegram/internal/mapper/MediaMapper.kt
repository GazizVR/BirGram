package org.gaziz.birgram.core.telegram.internal.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.model.AccentColor
import org.gaziz.birgram.core.telegram.api.model.StickerFormat
import org.gaziz.birgram.core.telegram.api.model.media.FileData
import org.gaziz.birgram.core.telegram.api.model.media.PhotoSize

fun TdApi.File.toFileData(): FileData {
    return FileData(
        id = this.id,
        path = this.local.path,
        canDownload = this.local.canBeDownloaded,
        size = this.size
    )
}

fun TdApi.AccentColor.toAccentColor(): AccentColor {
    return AccentColor(
        id = this.id,
        builtInAccentColorId = this.builtInAccentColorId
    )
}

fun TdApi.StickerFormat.toFormat(): StickerFormat {
    return when(this) {
        is TdApi.StickerFormatTgs -> StickerFormat.Tgs
        is TdApi.StickerFormatWebm -> StickerFormat.WebM
        else -> StickerFormat.WebP
    }
}

fun TdApi.PhotoSize.toSize(): PhotoSize {
    return PhotoSize(
        type = this.type,
        file = this.photo.toFileData(),
        width = this.width,
        height = this.height
    )
}
