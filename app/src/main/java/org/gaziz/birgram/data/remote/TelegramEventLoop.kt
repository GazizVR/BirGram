package org.gaziz.birgram.data.remote

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.domain.model.auth.AuthState
import org.gaziz.birgram.domain.repository.EventLoopRepository
import javax.inject.Inject

class TelegramEventLoop @Inject constructor(private val manager: TelegramManager): EventLoopRepository {
    private val _authState = MutableStateFlow<AuthState>(AuthState.WaitParams)
    override val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    override val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    override fun setErrorMessage(errorMessage: String?) {
        _errorMessage.value = errorMessage
    }

    override fun createEventLoop() {
        manager.createClient(
            { event ->
                when (event) {
                    is TdApi.Error -> {
                        Log.e("TDLib", "${event.code}: ${event.message}")
                        setErrorMessage(event.message)
                    }

                    is TdApi.UpdateAuthorizationState -> {
                        _authState.value = when (event.authorizationState) {
                            is TdApi.AuthorizationStateWaitTdlibParameters -> AuthState.WaitParams
                            is TdApi.AuthorizationStateWaitPhoneNumber -> AuthState.WaitPhoneNumber
                            is TdApi.AuthorizationStateWaitCode -> AuthState.WaitCode
                            is TdApi.AuthorizationStateWaitPassword -> AuthState.WaitPassword
                            is TdApi.AuthorizationStateReady -> AuthState.Ready
                            else -> AuthState.Other(event.authorizationState.toString())
                        }
                    }

                    is TdApi.Ok -> {
                        setErrorMessage(null)
                    }
                }
            },
            { throwable ->
                val message = throwable.localizedMessage ?: throwable.message ?: "unknown update handler exception"
                Log.e("TDLib", message)
                setErrorMessage(message)
            },
        )
    }
}