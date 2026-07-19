package org.gaziz.birgram.features.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.telegram.api.AuthService
import org.gaziz.birgram.core.telegram.api.ErrorService
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
    init {
        if(!manager.isClientActive()) {
            manager.createClient(
                { updateDispatcher.dispatch(it) },
                { errorService.setErrorFromException(it) }
            )
        }
    }
    val authState = authService.authState
    fun loadState() {
        viewModelScope.launch {
            authService.loadAuthState()
        }
    }
    fun initApplication() {
        viewModelScope.launch {
            manager.createClient(
                { updateDispatcher.dispatch(it) },
                { errorService.setErrorFromException(it) }
            )
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