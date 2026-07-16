package org.gaziz.birgram.core.telegram.model

data class FileData(
    val id: Int,
    val path: String,
    val canDownload: Boolean,
)