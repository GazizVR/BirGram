package org.gaziz.birgram.features.splash.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.internal.updaters.AuthDataSource
import org.gaziz.birgram.features.splash.domain.model.AppState
import javax.inject.Inject

class GetAppState @Inject constructor(
    private val tgAuth: AuthDataSource
) {
    operator fun invoke(): Flow<AppState?> {
        return tgAuth.authState.map { s ->
            when(s) {
                null -> null

                is TdApi.AuthorizationStateWaitTdlibParameters -> AppState.Init
                is TdApi.AuthorizationStateReady -> AppState.Ready

                is TdApi.AuthorizationStateLoggingOut -> AppState.Stopping
                is TdApi.AuthorizationStateClosing -> AppState.Stopping
                is TdApi.AuthorizationStateClosed -> AppState.Stopped

                else -> AppState.Auth
            }
        }
    }
}