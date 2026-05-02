package org.gaziz.birgram.presentation.auth.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.domain.model.auth.AuthData
import org.gaziz.birgram.data.remote.Auth
import org.gaziz.birgram.data.local.UserPreferences
import org.gaziz.birgram.presentation.auth.state.RequestState

class AuthViewModel(
    val userPreferences: UserPreferences,
    val tdLib: Auth
): ViewModel() {
    var isPhoneNumber by mutableStateOf(Pair(false,""))
    private val _loginState = MutableStateFlow<TdApi.AuthorizationState>(TdApi.AuthorizationStateWaitTdlibParameters())
    val loginState = _loginState.asStateFlow()

    private val _authHistory = MutableStateFlow<List<TdApi.AuthorizationState>>(emptyList())
    val authHistory = _authHistory.asStateFlow()

    private val _requestState = MutableStateFlow<RequestState>(RequestState.Init)
    val requestState = _requestState.asStateFlow()

    var isReverse by mutableStateOf(false)

    fun resetEmail(){
        _requestState.value = RequestState.Loading
        viewModelScope.launch {
            try {
                tdLib.resetEmail { _requestState.value = RequestState.Error(it) }
            } catch(e: Exception){
                val message = e.message ?: "unknown client exception"
                _requestState.value = RequestState.Error(message)
                Log.e("TDLib",message)
            }
        }
    }
    fun deleteAccount(password: String? = null){
        _requestState.value = RequestState.Loading
        viewModelScope.launch {
            try {
                tdLib.deleteAccount(password) { _requestState.value = RequestState.Error(it) }
            } catch(e: Exception){
                val message = e.message ?: "unknown client exception"
                _requestState.value = RequestState.Error(message)
                Log.e("TDLib",message)
            }
        }
    }
    fun passwordRecovery(
        code: String? = null,
        onOk: () -> Unit = {}
    ){
        _requestState.value = RequestState.Loading
        viewModelScope.launch {
            try {
                previousAuthHistory()
                tdLib.accountPasswordRecovery(code,{_requestState.value = RequestState.Error(it)},{onOk()})
            } catch(e: Exception){
                val message = e.message ?: "unknown client exception"
                _requestState.value = RequestState.Error(message)
                Log.e("TDLib",message)
            }
        }
    }

    fun nextLoginStep(
        state: TdApi.AuthorizationState,
        data: AuthData
    ){
        _requestState.value = RequestState.Loading
        viewModelScope.launch {
            try {
                tdLib.updateAuthState(state,data) { _requestState.value = RequestState.Error(it) }
            } catch(e: Exception){
                val message = e.message ?: "unknown client exception"
                _requestState.value = RequestState.Error(message)
                Log.e("TDLib",message)
            }
        }
    }

    fun addAuthToHistory(){
        viewModelScope.launch {
            val list = authHistory.value + loginState.value
            _authHistory.value = list
        }
    }

    var isNumber by mutableStateOf(false)
    fun previousAuthHistory(){
        isReverse = true
        _requestState.value = RequestState.Init
        viewModelScope.launch {
            val list = authHistory.value.toMutableList().apply { removeAt(lastIndex) }
            when(list.last()) {
                is TdApi.AuthorizationStateWaitCode -> {
                    isNumber = true
                    list[list.lastIndex] = TdApi.AuthorizationStateWaitPhoneNumber()
                }
                is TdApi.AuthorizationStateWaitEmailCode -> {
                    isNumber = true
                    list[list.lastIndex] = TdApi.AuthorizationStateWaitPhoneNumber()
                }
                else -> {}
            }
            _loginState.value = list.last()
            _authHistory.value = list
        }
    }

    fun resendCode(isUser: Boolean = true){
        _requestState.value = RequestState.Loading
        viewModelScope.launch {
            try {
                tdLib.resendCode(isUser) { _requestState.value = RequestState.Error(it) }
            } catch(it: Exception){
                val message = it.message ?: "unknown client exception"
                _requestState.value = RequestState.Error(message)
                Log.e("TDLib",message)
            }
        }
    }
    fun setPassword(
        oPassword: String,
        password: String,
        hint: String,
        isSetEmail: Boolean,
        newEmail: String?
    ){
        _requestState.value = RequestState.Loading
        viewModelScope.launch {
            try {
                tdLib.setPassword(oPassword,password,hint,isSetEmail,newEmail){_requestState.value = RequestState.Error(it)}
            } catch(e: Exception){
                val message = e.message ?: "unknown client exception"
                _requestState.value = RequestState.Error(message)
                Log.e("TDLib",message)
            }
        }
    }
}