package org.gaziz.birgram.features.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.gaziz.birgram.features.splash.domain.SplashRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val splashRepository: SplashRepository,
): ViewModel() {
    val appState = splashRepository.appState
    fun loadState() {
        viewModelScope.launch {
            splashRepository.loadAppState()
        }
    }
    fun initApplication() {
        viewModelScope.launch {
            splashRepository.initApplication()
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