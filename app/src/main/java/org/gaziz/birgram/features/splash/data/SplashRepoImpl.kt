package org.gaziz.birgram.features.splash.data

import android.os.Build
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.BuildConfig
import org.gaziz.birgram.core.telegram.ClientManager
import org.gaziz.birgram.core.telegram.data.source.TelegramAuth
import org.gaziz.birgram.features.splash.domain.repository.SplashRepository
import java.util.Locale
import javax.inject.Inject

class SplashRepoImpl @Inject constructor(
    private val manager: ClientManager,
    private val tgAuth: TelegramAuth
): SplashRepository {
    override fun initApplication() {
        manager.createClient()
    }
    override fun loadAppState() {
        val params = TdApi.GetAuthorizationState()
        manager.sendRequest(
            query = params,
            onResult = { obj ->
                if(obj is TdApi.AuthorizationState) {
                    tgAuth.setState(obj)
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