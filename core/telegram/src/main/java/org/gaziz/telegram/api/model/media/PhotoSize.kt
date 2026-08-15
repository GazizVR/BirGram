package org.gaziz.telegram.api.model.media

data class PhotoSize(
    val type: String,
    val file: FileData,
    val width: Int,
    val height: Int
)
