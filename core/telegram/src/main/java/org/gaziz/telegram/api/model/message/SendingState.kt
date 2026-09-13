package org.gaziz.telegram.api.model.message

sealed interface SendingState {
    object Pending: SendingState
    object Failed: SendingState
}