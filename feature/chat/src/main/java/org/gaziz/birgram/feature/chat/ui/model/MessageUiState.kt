package org.gaziz.birgram.feature.chat.ui.model

import org.gaziz.birgram.core.ui.model.MessageSenderInfo
import org.gaziz.telegram.api.model.message.SendingState

data class MessageUiState(
    val id: Long,
    val content: MessageContentInfo,
    val isOutgoing: Boolean,
    val date: String,
    val sender: MessageSenderInfo?,
    val sendingState: SendingState?
)
