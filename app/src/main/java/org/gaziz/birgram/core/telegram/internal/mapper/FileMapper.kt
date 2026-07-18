package org.gaziz.birgram.core.telegram.internal.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.model.FileData

fun TdApi.File.toFileData(): FileData {
    return FileData(
        id = this.id,
        path = this.local.path,
        canDownload = this.local.canBeDownloaded,
    )
}