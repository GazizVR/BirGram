package org.gaziz.birgram.core.telegram.api.model.message

sealed interface MessageSendingState {
    object Pending: MessageSendingState
    data class Failed(val canRetry: Boolean): MessageSendingState
}