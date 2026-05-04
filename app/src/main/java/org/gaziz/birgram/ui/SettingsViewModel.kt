package org.gaziz.birgram.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.domain.repository.EventLoopRepository
import org.gaziz.birgram.domain.usecase.GetIsDarkTheme
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val isDark: GetIsDarkTheme,
    private val eventLoopRepository: EventLoopRepository
): ViewModel() {
    init {
        eventLoopRepository.createEventLoop()
    }
    val isDarkTheme = isDark().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        true
    )
}