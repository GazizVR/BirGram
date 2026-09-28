package org.gaziz.birgram.feature.chat.ui.model

import org.gaziz.birgram.core.telegram.ui.model.MessageSenderUiState
import org.gaziz.birgram.core.telegram.api.model.message.MessageSendingState

data class MessageUiState(
    val id: Long,
    val content: MessageContentUiState,
    val isOutgoing: Boolean,
    val date: String,
    val sender: MessageSenderUiState?,
    val sendingState: MessageSendingState?,
    val originSenderTitle: String?,
    val canDeleteForSelf: Boolean,
    val canDeleteForAll: Boolean
)
