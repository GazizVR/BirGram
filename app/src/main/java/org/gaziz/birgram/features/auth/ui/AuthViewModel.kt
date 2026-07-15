package org.gaziz.birgram.features.auth.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.gaziz.birgram.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel() {

    val authState = authRepository.authState
    val errorMessage = authRepository.errorMessage

    fun setPhoneNumber(number: String){
       authRepository.setPhoneNumber(number) {
           authRepository.setErrorMessage(it)
       }
    }

    fun setCode(code: String){
        authRepository.checkCode(code){
            authRepository.setErrorMessage(it)
        }
    }

    fun resendCode() {
        authRepository.resendCode(true) {
            authRepository.setErrorMessage(it)
        }
    }

    fun setPassword(password: String){
        authRepository.checkPassword(password) {
            authRepository.setErrorMessage(it)
        }
    }

    fun restartAuth() {
        authRepository.setErrorMessage(null)
        authRepository.restartAuth()
    }

}