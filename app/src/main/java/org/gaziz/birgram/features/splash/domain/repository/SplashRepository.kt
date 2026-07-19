package org.gaziz.birgram.features.splash.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.core.telegram.api.model.auth.AuthState

interface SplashRepository {
    val authState: StateFlow<AuthState?>
    fun initApplication()

    fun loadAuthState()

    fun setParameters(
        databasePath: String,
        onError: (String) -> Unit,
    )
}