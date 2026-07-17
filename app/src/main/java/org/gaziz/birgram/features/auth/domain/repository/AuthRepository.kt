package org.gaziz.birgram.features.auth.domain.repository

interface AuthRepository {
    fun setPhoneNumber(phoneNumber: String)
    fun checkCode(code: String)
    fun resendCode(isUser: Boolean)
    fun checkPassword(password: String)
    fun restartAuth()
}