package org.gaziz.birgram.features.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.telegram.api.AuthService
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authService: AuthService,
): ViewModel() {
    val authState = authService.authState
    fun loadState() {
        viewModelScope.launch {
            authService.loadAuthState()
        }
    }
    fun initApplication() {
        viewModelScope.launch {
            authService.startAuthentication()
        }
    }
    fun setParams(
        dbPath: String,
        onErr: (String) -> Unit
    ){
        viewModelScope.launch {
            authService.setParameters(
                dbPath,
                onErr
            )
        }
    }
}