package org.gaziz.birgram.domain.repository

interface AuthRepository {

    fun setParameters(
        databasePath: String,
        onError: (String) -> Unit
    )

    fun setPhoneNumber(
        phoneNumber: String,
        onError: (String) -> Unit
    )

    fun checkAuthCode(
       code: String,
       onError: (String) -> Unit
    )

    fun setPassword(
        password: String,
        onError: (String) -> Unit
    )

    fun resendCode(
        isUser: Boolean,
        onError: (String) -> Unit
    )

}
