package org.gaziz.birgram.features.splash.data

import org.gaziz.birgram.core.telegram.api.AuthService
import org.gaziz.birgram.features.splash.domain.repository.SplashRepository
import javax.inject.Inject

class SplashRepoImpl @Inject constructor(
    private val authService: AuthService
): SplashRepository {

    override val authState = authService.authState

    override fun initApplication() {
        authService.startAuthentication()
    }

    override fun loadAuthState() {
        authService.loadAuthState()
    }

    override fun setParameters(
        databasePath: String,
        onError: (String) -> Unit
    ) {
        authService.setParameters(databasePath,onError)
    }
}