package org.gaziz.birgram.core.telegram.api

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.core.telegram.api.model.auth.AuthState

interface AuthService {
    fun getDefaultCodeLength(): Int
    val authState: StateFlow<AuthState?>
    fun setAuthState(state: AuthState)

    fun startAuthentication()

    fun loadAuthState()
    fun restartAuth()

    fun setParameters(
        databasePath: String,
        onError: (String) -> Unit
    )
    fun setPhoneNumber(phoneNumber: String)
    fun checkCode(code: String)
    fun resendCode(isUser: Boolean)
    fun checkPassword(password: String)

    fun logOut(onOk: () -> Unit)

}