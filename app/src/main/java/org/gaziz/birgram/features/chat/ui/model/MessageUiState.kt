package org.gaziz.birgram.features.chat.ui.model

import org.gaziz.birgram.core.telegram.api.model.message.MessageContent

data class MessageUiState(
    val content: MessageContent,
    val isOutgoing: Boolean,
    val date: String,
)
