package org.gaziz.birgram.features.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.gaziz.birgram.features.splash.domain.repository.SplashRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val splashRepository: SplashRepository,
): ViewModel() {
    val authState = splashRepository.authState
    fun loadState() {
        viewModelScope.launch {
            splashRepository.loadAuthState()
        }
    }
    fun initApplication() {
        viewModelScope.launch {
            splashRepository.initApplication()
        }
    }
    fun setParams(
        dbPath: String,
        onErr: (String) -> Unit
    ){
        viewModelScope.launch {
            splashRepository.setParameters(
                dbPath,
                onErr
            )
        }
    }
}