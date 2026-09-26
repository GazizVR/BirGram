package org.gaziz.birgram.feature.chat.ui.model

import org.gaziz.birgram.core.ui.model.MessageSenderInfo
import org.gaziz.birgram.core.telegram.api.model.message.MessageSendingState

data class MessageUiState(
    val id: Long,
    val content: MessageContentInfo,
    val isOutgoing: Boolean,
    val date: String,
    val sender: MessageSenderInfo?,
    val sendingState: MessageSendingState?,
    val originSenderTitle: String?,
    val canDeleteForSelf: Boolean,
    val canDeleteForAll: Boolean
)
