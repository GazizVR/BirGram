package org.gaziz.birgram.core.telegram.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.AuthService
import org.gaziz.birgram.core.telegram.api.ErrorService
import org.gaziz.birgram.core.telegram.api.model.auth.AuthState
import org.gaziz.birgram.core.telegram.internal.ClientManager
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    private val manager: ClientManager,
    private val errorService: ErrorService
): AuthService {
    companion object {
        const val DEFAULT_CODE_LENGTH = 5
    }

    override fun getDefaultCodeLength(): Int {
        return DEFAULT_CODE_LENGTH
    }
    private val _authState = MutableStateFlow<AuthState?>(null)
    override val authState: StateFlow<AuthState?> = _authState.asStateFlow()

    override fun setAuthState(state: AuthState) {
        _authState.update { state }
    }

    override fun setParameters(
        databasePath: String,
        onError: (String) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun setPhoneNumber(
        phoneNumber: String,
    ) {
        val phoneNumber = TdApi.SetAuthenticationPhoneNumber().apply {
            this.phoneNumber = phoneNumber
            this.settings = null
        }
        manager.sendRequest(
            phoneNumber,
            { errorService.setError(it) },
            { if(it is TdApi.Ok) errorService.setError(null) }
        )
    }

    override fun checkCode(
        code: String,
    ) {
        val code = TdApi.CheckAuthenticationCode().apply {
            this.code = code
        }
        manager.sendRequest(
            code,
            { errorService.setError(it) },
            { if(it is TdApi.Ok) errorService.setError(null) }
        )
    }

    override fun resendCode(
        isUser: Boolean,
    ) {
        val parameters = TdApi.ResendAuthenticationCode().apply {
            reason = if (isUser) TdApi.ResendCodeReasonUserRequest()
            else TdApi.ResendCodeReasonVerificationFailed()
        }
        manager.sendRequest(
            parameters,
            { errorService.setError(it) },
            { if(it is TdApi.Ok) errorService.setError(null) }
        )
    }

    override fun checkPassword(password: String) {
        val parameters = TdApi.CheckAuthenticationPassword().apply {
            this.password = password
        }
        manager.sendRequest(
            parameters,
            { errorService.setError(it) },
            { if(it is TdApi.Ok) errorService.setError(null) }
        )
    }

    override fun restartAuth() {
        setAuthState(AuthState.WaitPhoneNumber)
    }

    override fun startAuthorization() {
        TODO("Not yet implemented")
    }

}