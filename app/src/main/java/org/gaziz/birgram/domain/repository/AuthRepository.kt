package org.gaziz.birgram.domain.repository

import org.gaziz.birgram.domain.model.auth.AuthData
import org.gaziz.birgram.domain.model.auth.AuthState

interface AuthRepository {

    fun updateAuthState(
        state: AuthState,
        data: AuthData,
        onError: (String) -> Unit,
        databasePath: String
    )

    fun resendCode(
        isUser: Boolean,
        onError: (String) -> Unit
    )

    fun deleteAccount(
        password: String? = null,
        onError: (String) -> Unit
    )

    fun resetEmail(onError: (String) -> Unit)

    fun accountPasswordRecovery(
        code: String? = null,
        onError: (String) -> Unit,
        onOk: () -> Unit
    )

    fun setPassword(
        oPassword: String,
        password: String,
        hint: String,
        isSetEmail: Boolean = false,
        newEmail: String? = null,
        onError: (String) -> Unit
    )

}
