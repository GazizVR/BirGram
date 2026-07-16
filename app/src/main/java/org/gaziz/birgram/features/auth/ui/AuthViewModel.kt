package org.gaziz.birgram.features.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.features.auth.domain.repository.AuthRepository
import org.gaziz.birgram.features.auth.domain.usecase.GetAuthState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getAuthState: GetAuthState
): ViewModel() {

    val authState = getAuthState().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        null
    )
    val errorMessage = authRepository.errorMessage

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