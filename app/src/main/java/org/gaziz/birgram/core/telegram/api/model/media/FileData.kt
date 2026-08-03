package org.gaziz.birgram.core.telegram.api.model.media

data class FileData(
    val id: Int,
    val path: String,
    val canDownload: Boolean,
)