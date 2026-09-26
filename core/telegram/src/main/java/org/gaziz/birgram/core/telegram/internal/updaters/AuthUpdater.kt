package org.gaziz.birgram.core.telegram.internal.updaters

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.AuthService
import org.gaziz.birgram.core.telegram.internal.mapper.toAuthState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUpdater @Inject constructor(
    private val authService: AuthService,
) {
    fun onAuthStateUpdate(
        update: TdApi.UpdateAuthorizationState
    ) {
        authService.setAuthState(
            update.authorizationState.toAuthState(
                authService.getDefaultCodeLength()
            )
        )
    }

}