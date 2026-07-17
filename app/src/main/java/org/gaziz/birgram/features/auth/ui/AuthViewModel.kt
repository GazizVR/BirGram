package org.gaziz.birgram.features.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.features.auth.domain.repository.AuthRepository
import org.gaziz.birgram.features.auth.domain.usecase.GetAuthState
import org.gaziz.birgram.features.auth.domain.usecase.GetErrorMessage
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    getAuthState: GetAuthState,
    getErrorMessage: GetErrorMessage,
    private val authRepository: AuthRepository
): ViewModel() {

    val errorMessage = getErrorMessage().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        null
    )

    val authState = getAuthState().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        null
    )

    fun setPhoneNumber(number: String){
       authRepository.setPhoneNumber(number)
    }

    fun setCode(code: String){
        authRepository.checkCode(code)
    }

    fun resendCode() {
        authRepository.resendCode(true)
    }

    fun setPassword(password: String){
        authRepository.checkPassword(password)
    }

    fun restartAuth() {
        authRepository.restartAuth()
    }

}