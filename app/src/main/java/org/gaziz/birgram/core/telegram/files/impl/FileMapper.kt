package org.gaziz.birgram.core.telegram.files.impl

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.files.api.model.FileData

fun TdApi.File.toFileData(): FileData {
    return FileData(
        id = this.id,
        path = this.local.path,
        canDownload = this.local.canBeDownloaded,
    )
}