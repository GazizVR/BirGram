package org.gaziz.birgram.features.auth.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.gaziz.birgram.features.auth.domain.repository.AuthRepository
import org.gaziz.birgram.core.telegram.domain.repository.EventLoopRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val eventLoopRepository: EventLoopRepository
): ViewModel() {

    val authState = eventLoopRepository.authState

    val errorMessage = eventLoopRepository.errorMessage

    fun setPhoneNumber(number: String){
       authRepository.setPhoneNumber(number) {
           eventLoopRepository.setErrorMessage(it)
       }
    }

    fun setCode(code: String){
        authRepository.checkCode(code){
            eventLoopRepository.setErrorMessage(it)
        }
    }

    fun resendCode() {
        authRepository.resendCode(true) {
            eventLoopRepository.setErrorMessage(it)
        }
    }

    fun setPassword(password: String){
        authRepository.checkPassword(password) {
            eventLoopRepository.setErrorMessage(it)
        }
    }

    fun restartAuth() {
        eventLoopRepository.setErrorMessage(null)
        eventLoopRepository.restartAuth()
    }

}