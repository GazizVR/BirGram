package org.gaziz.birgram.core.telegram.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.model.FileData

fun TdApi.File.toFileData(): FileData {
    return FileData(
        id = this.id,
        path = this.local.path,
        canDownload = this.local.canBeDownloaded,
        isDownloading = this.local.isDownloadingActive,
        isCompleted = this.local.isDownloadingCompleted
    )
}