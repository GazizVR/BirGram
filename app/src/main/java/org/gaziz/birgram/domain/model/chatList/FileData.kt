package org.gaziz.birgram.domain.model.chatList

data class FileData(
    val id: Int,
    val path: String,
    val canDownload: Boolean,
    val isDownloading: Boolean,
    val isCompleted: Boolean
)
