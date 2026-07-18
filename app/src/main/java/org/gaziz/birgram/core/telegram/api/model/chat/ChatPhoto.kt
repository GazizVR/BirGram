package org.gaziz.birgram.core.telegram.api.model.chat

import org.gaziz.birgram.core.telegram.api.model.FileData

data class ChatPhoto(
    val miniThumbnail: ByteArray?,
    val small: FileData
)