package org.gaziz.birgram.features.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gaziz.birgram.features.splash.domain.repository.SplashRepository
import org.gaziz.birgram.features.splash.domain.usecase.GetAppState
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val splashRepository: SplashRepository,
    private val getAppState: GetAppState
): ViewModel() {
    val appState = getAppState().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        null
    )
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