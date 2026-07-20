package org.gaziz.birgram.features.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.telegram.api.AuthService
import org.gaziz.birgram.core.telegram.api.ErrorService
import org.gaziz.birgram.core.telegram.api.model.auth.AuthState
import org.gaziz.birgram.core.telegram.internal.ClientManager
import org.gaziz.birgram.core.telegram.internal.UpdateDispatcher
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authService: AuthService,
    private val manager: ClientManager,
    private val updateDispatcher: UpdateDispatcher,
    private val errorService: ErrorService
): ViewModel() {
    private fun startNonReadyAuthStateCheck(
        onNonReadyAuthState: () -> Unit
    ) {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        scope.launch {
            authService.authState.collect { s ->
                if(s is AuthState.Closed) {
                    onNonReadyAuthState()
                    scope.cancel()
                    return@collect
                }
                if(s is AuthState.LoggingOut) {
                    onNonReadyAuthState()
                }
            }
        }
    }
    fun initApplication(
        onNonReady: () -> Unit,
        isForce: Boolean = false
    ) {
        if(
            !manager.isClientActive() ||
            isForce
        ) {
            manager.createClient(
                { updateDispatcher.dispatch(it) },
                { errorService.setErrorFromException(it) }
            )
            startNonReadyAuthStateCheck(onNonReady)
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