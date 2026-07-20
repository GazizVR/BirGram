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

        onError: (ResponseData.Error) -> Unit = {},
        onFile: (FileData) -> Unit = {},
    )
    fun getFile(
        fileId: Int,
        onError: (ResponseData.Error) -> Unit = {},
        onFile: (FileData) -> Unit
    )
}