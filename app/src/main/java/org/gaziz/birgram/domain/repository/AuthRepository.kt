package org.gaziz.birgram.domain.repository

interface AuthRepository {

    fun setPhoneNumber(
        phoneNumber: String,
        onError: (String?) -> Unit
    )

    fun checkCode(
       code: String,
       onError: (String?) -> Unit
    )

    fun checkPassword(
        password: String,
        onError: (String?) -> Unit
    )

    fun resendCode(
        isUser: Boolean,
        onError: (String?) -> Unit
    )

}
