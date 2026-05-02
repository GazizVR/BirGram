package org.gaziz.birgram.presentation.auth.state

sealed class RequestState {
    object Init: RequestState()
    object Loading: RequestState()
    data class Error(val message: String): RequestState()
}
