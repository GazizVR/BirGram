package org.gaziz.telegram.api

import org.gaziz.telegram.api.model.ResponseData
import org.gaziz.telegram.api.model.media.FileData

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