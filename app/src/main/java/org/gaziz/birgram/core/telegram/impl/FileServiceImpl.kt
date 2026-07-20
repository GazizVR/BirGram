package org.gaziz.birgram.core.telegram.impl

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.FileService
import org.gaziz.birgram.core.telegram.api.model.FileData
import org.gaziz.birgram.core.telegram.api.model.ResponseData
import org.gaziz.birgram.core.telegram.internal.ClientManager
import org.gaziz.birgram.core.telegram.internal.mapper.toFileData
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