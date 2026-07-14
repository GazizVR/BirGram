package org.gaziz.birgram.features.splash.domain

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.domain.model.auth.AuthState

interface SplashRepository {
    val authState: StateFlow<AuthState?>
    fun getAuthState()
    fun setParameters(
        databasePath: String,
        onError: (String) -> Unit
    )
}