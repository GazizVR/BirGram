package org.gaziz.birgram.features.auth.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.features.auth.domain.model.AuthState

interface AuthRepository {
    val authState: StateFlow<AuthState>
    val errorMessage: StateFlow<String?>
    fun setErrorMessage(msg: String?)

    fun setPhoneNumber(
        phoneNumber: String,
        onError: (String) -> Unit
    )

    fun checkCode(
       code: String,
       onError: (String) -> Unit
    )

    fun checkPassword(
        password: String,
        onError: (String) -> Unit
    )

    fun resendCode(
        isUser: Boolean,
        onError: (String) -> Unit
    )
    fun restartAuth()
}