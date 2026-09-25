package org.gaziz.telegram.api.model.message

sealed interface MessageSendingState {
    object Pending: MessageSendingState
    object Failed: MessageSendingState
}