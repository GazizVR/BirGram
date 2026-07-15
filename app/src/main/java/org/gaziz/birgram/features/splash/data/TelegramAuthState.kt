package org.gaziz.birgram.features.splash.data

import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.BuildConfig
import org.gaziz.birgram.core.telegram.TelegramManager
import org.gaziz.birgram.features.splash.domain.SplashRepository
import org.gaziz.birgram.features.splash.domain.model.AppState
import java.util.Locale
import javax.inject.Inject

class TelegramAuthState @Inject constructor(
    private val manager: TelegramManager,
): SplashRepository {
    init {
        CoroutineScope(Dispatchers.Main).launch {
            collectUpdates()
        }
    }
    private suspend fun collectUpdates() {
        manager.update.collect { u ->
            if(u is TdApi.UpdateAuthorizationState) {
                _appState.value = when (u.authorizationState) {
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
    private val _appState = MutableStateFlow<AppState?>(null)
    override val appState = _appState.asStateFlow()
    override fun initApplication() {
        manager.createClient()
    }
    override fun loadAppState() {
        val params = TdApi.GetAuthorizationState()
        manager.sendRequest(
            query = params,
            onResult = { obj ->
                if(obj is TdApi.AuthorizationState) {
                    _appState.update {
                        when (obj) {
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
        )
    }

    override fun setParameters(
        databasePath: String,
        onError: (String) -> Unit
    ) {
        val parameters = TdApi.SetTdlibParameters().apply {
            apiId = BuildConfig.API_ID.toInt()
            apiHash = BuildConfig.API_HASH
            filesDirectory = databasePath + "_files"
            databaseDirectory = databasePath
            useMessageDatabase = true
            useFileDatabase = true
            useChatInfoDatabase = true
            useSecretChats = false
            systemLanguageCode = "${Locale.getDefault().language}-${Locale.getDefault().country}"
            deviceModel = Build.MODEL
            applicationVersion = "0.1"
        }
        manager.sendRequest(
            query = parameters,
            onError = { onError(it.message) },
        )
    }
}