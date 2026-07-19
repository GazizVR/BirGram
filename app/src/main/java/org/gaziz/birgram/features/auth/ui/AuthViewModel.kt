package org.gaziz.birgram.features.auth.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.gaziz.birgram.core.telegram.api.AuthService
import org.gaziz.birgram.core.telegram.api.ErrorService
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService,
    private val errorService: ErrorService
): ViewModel() {

    val errorMessage = errorService.error

    val authState = authService.authState

    fun setPhoneNumber(number: String){
       authService.setPhoneNumber(number)
    }

    fun setCode(code: String){
        authService.checkCode(code)
    }

    fun resendCode() {
        authService.resendCode(true)
    }

    fun setPassword(password: String){
        authService.checkPassword(password)
    }

    fun restartAuth() {
        authService.restartAuth()
    }

}