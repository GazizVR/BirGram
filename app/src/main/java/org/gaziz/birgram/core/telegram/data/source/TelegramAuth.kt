package org.gaziz.birgram.core.telegram.data.source

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelegramAuth @Inject constructor() {
    private val _authState = MutableStateFlow<TdApi.AuthorizationState?>(null)
    val authState = _authState.asStateFlow()

    fun setState(state: TdApi.AuthorizationState) {
        _authState.update { state }
    }

    fun onUpdateAuthState(
        update: TdApi.UpdateAuthorizationState
    ) {
        setState(update.authorizationState)
    }
}