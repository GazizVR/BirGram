package org.gaziz.birgram.core.telegram.domain.model.chat

import org.gaziz.birgram.core.telegram.domain.model.FileData

data class ChatPhoto(
    val miniThumbnail: ByteArray?,
    val small: FileData
)