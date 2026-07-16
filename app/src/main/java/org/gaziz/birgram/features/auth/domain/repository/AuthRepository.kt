package org.gaziz.birgram.features.auth.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val errorMessage: StateFlow<String?>
    fun setPhoneNumber(phoneNumber: String)
    fun checkCode(code: String)
    fun resendCode(isUser: Boolean)
    fun checkPassword(password: String)
    fun restartAuth()
}