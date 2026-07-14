package org.gaziz.birgram.features.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.gaziz.birgram.domain.repository.EventLoopRepository
import org.gaziz.birgram.features.splash.domain.SplashRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val splashRepository: SplashRepository,
    private val eventLoopRepository: EventLoopRepository
): ViewModel() {
    val localState = splashRepository.authState
    val remoteState = eventLoopRepository.authState
    fun loadState() {
        viewModelScope.launch {
            splashRepository.getAuthState()
        }
    }
    fun initEventLoop() {
        viewModelScope.launch {
            eventLoopRepository.createEventLoop()
        }
    }
    fun setParams(
        dbPath: String,
        onErr: (String) -> Unit,
    ){
        viewModelScope.launch {
            splashRepository.setParameters(
                dbPath,
                onErr
            )
        }
    }
}