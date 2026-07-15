package org.gaziz.birgram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.core.datastore.UserPreferencesRepository
import org.gaziz.birgram.core.telegram.TelegramManager
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository,
    manager: TelegramManager
): ViewModel() {
    init {
        manager.createClient()
    }
    val isDarkTheme = userPreferencesRepository.isDark.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        true
    )
}