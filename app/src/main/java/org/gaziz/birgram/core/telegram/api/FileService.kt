package org.gaziz.birgram.core.telegram.api

import org.gaziz.birgram.core.telegram.api.model.FileData
import org.gaziz.birgram.core.telegram.api.model.ResponseData

interface FileService {
    fun downloadFile(
        fileId: Int,
        priority: Int = 32,
        offset: Long = 0,
        limit: Long = 0,
        synchronous: Boolean = true,

        onFile: (FileData) -> Unit = {},
        onError: (ResponseData.Error) -> Unit = {}
    )
}