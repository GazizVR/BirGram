package org.gaziz.birgram.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gaziz.birgram.domain.repository.AuthRepository
import org.gaziz.birgram.domain.repository.EventLoopRepository
import org.gaziz.birgram.domain.repository.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val eventLoopRepository: EventLoopRepository,
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    init {
        eventLoopRepository.createEventLoop()
    }
    val isDarkTheme = userPreferencesRepository.isDark.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        true
    )

    fun switchTheme(isDark: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.switchIsDark(isDark)
        }
    }
    val authState = eventLoopRepository.authState
    val isRegistered = userPreferencesRepository.isRegistered.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )
    fun setParams(databasePath: String) {
       authRepository.setParameters(
           databasePath
       ) {
           eventLoopRepository.setErrorMessage(it)
       }
    }

}
