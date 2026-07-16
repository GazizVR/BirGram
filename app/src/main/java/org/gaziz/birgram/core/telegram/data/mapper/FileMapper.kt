package org.gaziz.birgram.core.telegram.data.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.model.FileData

fun TdApi.File.toFileData(): FileData {
    return FileData(
        id = this.id,
        path = this.local.path,
        canDownload = this.local.canBeDownloaded,
    )
}