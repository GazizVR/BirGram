package org.gaziz.birgram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.core.telegram.domain.repository.EventLoopRepository
import org.gaziz.birgram.core.datastore.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository,
    eventLoopRepository: EventLoopRepository
): ViewModel() {
    init {
        eventLoopRepository.createEventLoop()
    }
    val isDarkTheme = userPreferencesRepository.isDark.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        true
    )
}