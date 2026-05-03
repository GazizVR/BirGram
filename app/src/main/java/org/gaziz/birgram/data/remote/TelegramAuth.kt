package org.gaziz.birgram.data.remote

import android.os.Build
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.BuildConfig
import org.gaziz.birgram.domain.repository.AuthRepository
import java.util.Locale
import javax.inject.Inject

class TelegramAuth @Inject constructor(private val manager: TelegramManager): AuthRepository {

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
        manager.sendRequest(parameters, onError)
    }

    override fun setPhoneNumber(
        phoneNumber: String,
        onError: (String) -> Unit
    ) {
        val phoneNumber = TdApi.SetAuthenticationPhoneNumber().apply {
            this.phoneNumber = phoneNumber
            this.settings = null
        }
        manager.sendRequest(phoneNumber, onError)
    }

    override fun checkAuthCode(
        code: String,
        onError: (String) -> Unit
    ) {
        val code = TdApi.CheckAuthenticationCode().apply {
            this.code = code
        }
        manager.sendRequest(code, onError)
    }

    override fun resendCode(
        isUser: Boolean,
        onError: (String) -> Unit
    ) {
        val parameters = TdApi.ResendAuthenticationCode().apply {
            reason = if (isUser) TdApi.ResendCodeReasonUserRequest()
            else TdApi.ResendCodeReasonVerificationFailed()
        }
        manager.sendRequest(parameters, onError)
    }

    override fun setPassword(
        password: String,
        onError: (String) -> Unit
    ) {
        val parameters = TdApi.CheckAuthenticationPassword().apply {
            this.password = password
        }
        manager.sendRequest(parameters, onError)
    }

}
