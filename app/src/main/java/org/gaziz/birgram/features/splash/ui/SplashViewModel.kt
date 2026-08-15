package org.gaziz.birgram.features.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.gaziz.telegram.api.AuthService
import org.gaziz.telegram.api.ErrorService
import org.gaziz.telegram.internal.ClientManager
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authService: AuthService,
    private val errorService: ErrorService,
    private val manager: ClientManager
): ViewModel() {
    fun initApplication(
        onNonReady: () -> Unit,
        isForce: Boolean = false
    ) {
        if(
            !manager.isClientActive() ||
            isForce
        ) {
            authService.onLoggingOut = onNonReady
            manager.createClient()
        }
    }
    val authState = authService.authState
    fun loadState() {
        viewModelScope.launch {
            authService.loadAuthState()
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