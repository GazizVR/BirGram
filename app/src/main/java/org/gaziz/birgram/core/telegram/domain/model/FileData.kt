package org.gaziz.birgram.core.telegram.domain.model

data class FileData(
    val id: Int,
    val path: String,
    val canDownload: Boolean,
    val isDownloading: Boolean,
    val isCompleted: Boolean
)