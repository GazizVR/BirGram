package org.gaziz.telegram.impl

import org.drinkless.tdlib.TdApi
import org.gaziz.telegram.api.FileService
import org.gaziz.telegram.api.model.ResponseData
import org.gaziz.telegram.api.model.media.FileData
import org.gaziz.telegram.internal.ClientManager
import org.gaziz.telegram.internal.mapper.toFileData
import javax.inject.Inject

class FileServiceImpl @Inject constructor(
    private val manager: ClientManager
): FileService {
    override fun downloadFile(
        fileId: Int,

        priority: Int,
        offset: Long,
        limit: Long,
        synchronous: Boolean,

        onError: (ResponseData.Error) -> Unit,
        onFile: (FileData) -> Unit,
    ) {
        manager.sendRequest(
            TdApi.DownloadFile().apply {
                this.fileId = fileId
                this.priority = priority
                this.offset = offset
                this.limit = limit
                this.synchronous = synchronous
            },
            onError
        ) { obj ->
            if (obj is TdApi.File) {
                onFile(obj.toFileData())
            }
        }
    }

    override fun getFile(
        fileId: Int,
        onError: (ResponseData.Error) -> Unit,
        onFile: (FileData) -> Unit
    ) {
        manager.sendRequest(
            TdApi.GetFile().apply {
                this.fileId = fileId
            },
            onError
        ) { obj ->
            if(obj is TdApi.File) {
                onFile(obj.toFileData())
            }
        }
    }

}