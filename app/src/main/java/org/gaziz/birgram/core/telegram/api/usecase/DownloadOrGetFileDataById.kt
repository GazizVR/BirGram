package org.gaziz.birgram.core.telegram.api.usecase

import org.gaziz.birgram.core.telegram.api.FileService
import org.gaziz.birgram.core.telegram.api.model.media.FileData
import javax.inject.Inject

class DownloadOrGetFileDataById @Inject constructor(
    private val fileService: FileService
) {
    operator fun invoke(
        fileId: Int,
        onFile: (FileData) -> Unit
    ) {
        fileService.getFile(
            fileId,
        ) { file ->
            if(file.path.isNotBlank()) {
                onFile(file)
            } else {
                fileService.downloadFile(
                    fileId,
                    onFile = { onFile(it) }
                )
            }
        }
    }
}