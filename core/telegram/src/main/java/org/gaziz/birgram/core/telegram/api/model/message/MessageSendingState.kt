package org.gaziz.birgram.core.telegram.api.model.message

sealed interface MessageSendingState {
    object Pending: MessageSendingState
    object Failed: MessageSendingState
}