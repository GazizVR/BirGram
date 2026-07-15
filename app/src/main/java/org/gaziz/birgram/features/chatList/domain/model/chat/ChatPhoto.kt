package org.gaziz.birgram.features.chatList.domain.model.chat

import org.gaziz.birgram.features.chatList.domain.model.FileData

data class ChatPhoto(
    val miniThumbnail: ByteArray?,
    val small: FileData
)