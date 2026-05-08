package org.gaziz.birgram.domain.model.chat

import org.gaziz.birgram.domain.model.FileData

data class ChatPhoto(
    val miniThumbnail: ByteArray?,
    val small: FileData
)