package org.gaziz.birgram.core.telegram.files.api.model

data class FileData(
    val id: Int,
    val path: String,
    val canDownload: Boolean,
)