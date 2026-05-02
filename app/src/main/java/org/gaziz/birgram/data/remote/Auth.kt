package org.gaziz.birgram.data.remote

import android.os.Build
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.BuildConfig
import org.gaziz.birgram.domain.model.auth.AuthData
import org.gaziz.birgram.domain.model.auth.AuthState
import org.gaziz.birgram.domain.repository.AuthRepository
import java.util.*

class Auth(val client: Client): AuthRepository {

    override fun setPassword(
        oPassword: String,
        password: String,
        hint: String,
        isSetEmail: Boolean,
        newEmail: String?,
        onError: (String) -> Unit
    ) {
        val params = TdApi.SetPassword().apply {
            oldPassword = oPassword
            newPassword = password
            newHint = hint
            setRecoveryEmailAddress = isSetEmail
            newRecoveryEmailAddress = newEmail
        }
        Helper.sendRequest(params, onError, client = client)
    }

    override fun accountPasswordRecovery(
        code: String?,
        onError: (String) -> Unit,
        onOk: () -> Unit
    ) {
        val parameters = if (code == null) {
            TdApi.RequestAuthenticationPasswordRecovery()
        } else {
            TdApi.CheckAuthenticationPasswordRecoveryCode().apply { recoveryCode = code }
        }
        Helper.sendRequest(parameters, onError, onOk, client = client)
    }

    override fun resendCode(isUser: Boolean, onError: (String) -> Unit) {
        val parameters = TdApi.ResendAuthenticationCode().apply {
            reason = if (isUser) TdApi.ResendCodeReasonUserRequest()
            else TdApi.ResendCodeReasonVerificationFailed()
        }
        Helper.sendRequest(parameters, onError, client = client)
    }

    override fun resetEmail(onError: (String) -> Unit) {
        val request = TdApi.ResetAuthenticationEmailAddress()
        Helper.sendRequest(request, onError, client = client)
    }

    override fun deleteAccount(password: String?, onError: (String) -> Unit) {
        val params = TdApi.DeleteAccount().apply { if (password != null) this.password = password }
        Helper.sendRequest(params, onError, client = client)
    }

    override fun updateAuthState(
        state: AuthState,
        data: AuthData,
        onError: (String) -> Unit,
        databasePath: String
    ) {
        when (state) {
            AuthState.Parameters -> {
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
                Helper.sendRequest(parameters, onError, client = client)
            }

            AuthState.PhoneNumber  -> {
                val phoneNumber = TdApi.SetAuthenticationPhoneNumber().apply {
                    phoneNumber = (data as? AuthData.PhoneNumber)?.phoneNumber ?: ""
                    settings = null
                }
                Helper.sendRequest(phoneNumber, onError, client = client)
            }

            AuthState.Code -> {
                val code = TdApi.CheckAuthenticationCode().apply {
                    this.code = (data as? AuthData.Code)?.code ?: ""
                }
                Helper.sendRequest(code, onError, client = client)
            }

            AuthState.Email -> {
                val parameters = TdApi.SetAuthenticationEmailAddress().apply {
                    this.emailAddress = (data as? AuthData.Email)?.email ?: ""
                }
                Helper.sendRequest(parameters, onError, client = client)
            }

            AuthState.Password -> {
                val parameters = TdApi.CheckAuthenticationPassword().apply {
                    this.password = (data as? AuthData.Password)?.password ?: ""
                }
                Helper.sendRequest(parameters, onError, client = client)
            }

            AuthState.EmailCode -> {
                val parameters = TdApi.CheckAuthenticationCode().apply {
                    this.code = (data as? AuthData.EmailCode)?.emailCode ?: ""
                }
                Helper.sendRequest(parameters, onError, client = client)
            }

            AuthState.Registration -> {
                val user = TdApi.RegisterUser().apply {
                    this.firstName = (data as? AuthData.Registration)?.firstName ?: ""
                    this.lastName = (data as? AuthData.Registration)?.lastName ?: ""
                    this.disableNotification = (data as? AuthData.Registration)?.disableNotification ?: false
                }
                Helper.sendRequest(user, onError, client = client)
            }

            AuthState.Ready -> {}
        }
    }

}
