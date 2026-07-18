package org.gaziz.birgram.core.telegram.chats.api.model

import org.gaziz.birgram.core.telegram.files.api.model.FileData

data class ChatPhoto(
    val miniThumbnail: ByteArray?,
    val small: FileData
)