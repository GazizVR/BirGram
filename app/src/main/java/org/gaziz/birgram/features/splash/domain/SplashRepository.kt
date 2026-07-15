package org.gaziz.birgram.features.splash.domain

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.features.splash.domain.model.AppState

interface SplashRepository {
    val appState: StateFlow<AppState?>

    fun initApplication()

    fun loadAppState()
    fun setParameters(
        databasePath: String,
        onError: (String) -> Unit,
    )
}