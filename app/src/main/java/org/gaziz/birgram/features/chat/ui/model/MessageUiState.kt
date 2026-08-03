package org.gaziz.birgram.features.chat.ui.model

import org.gaziz.birgram.core.telegram.api.model.message.MessageSenderInfo

data class MessageUiState(
    val id: Long,
    val content: MessageContentInfo,
    val isOutgoing: Boolean,
    val date: String,
    val sender: MessageSenderInfo?
)
