package org.gaziz.birgram.features.splash.data

import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.BuildConfig
import org.gaziz.birgram.core.telegram.TelegramManager
import org.gaziz.birgram.features.auth.domain.model.AuthCodeInfo
import org.gaziz.birgram.features.auth.domain.model.AuthCodeType
import org.gaziz.birgram.features.auth.domain.model.AuthPasswordInfo
import org.gaziz.birgram.features.auth.domain.model.AuthState
import org.gaziz.birgram.features.auth.domain.model.CodeType
import org.gaziz.birgram.features.splash.domain.SplashRepository
import java.util.Locale
import javax.inject.Inject

class TelegramAuthState @Inject constructor(
    private val manager: TelegramManager,
): SplashRepository {
    private val _authState = MutableStateFlow<AuthState?>(null)
    override val authState = _authState.asStateFlow()
    override fun getAuthState() {
        val params = TdApi.GetAuthorizationState()
        manager.sendRequest(
            query = params,
            onResult = { obj ->
                if(obj is TdApi.AuthorizationState) {
                    _authState.update {
                        when (obj) {
                            is TdApi.AuthorizationStateWaitTdlibParameters -> AuthState.WaitParams
                            is TdApi.AuthorizationStateWaitPhoneNumber -> AuthState.WaitPhoneNumber
                            is TdApi.AuthorizationStateWaitCode -> {
                                val codeInfo = obj.codeInfo
                                val codeType: (TdApi.AuthenticationCodeType) -> AuthCodeType = {
                                    when (it) {
                                        is TdApi.AuthenticationCodeTypeCall -> AuthCodeType(
                                            CodeType.Call,
                                            it.length
                                        )

                                        is TdApi.AuthenticationCodeTypeTelegramMessage -> AuthCodeType(
                                            CodeType.Telegram,
                                            it.length
                                        )

                                        is TdApi.AuthenticationCodeTypeSms -> AuthCodeType(
                                            CodeType.SMS,
                                            it.length
                                        )

                                        is TdApi.AuthenticationCodeTypeFlashCall -> AuthCodeType(
                                            CodeType.FlashCall,
                                            manager.getDefaultCodeLength()
                                        )

                                        is TdApi.AuthenticationCodeTypeMissedCall -> AuthCodeType(
                                            CodeType.MissedCall,
                                            it.length
                                        )

                                        is TdApi.AuthenticationCodeTypeFragment -> AuthCodeType(
                                            CodeType.Fragment,
                                            it.length
                                        )

                                        is TdApi.AuthenticationCodeTypeFirebaseAndroid -> AuthCodeType(
                                            CodeType.FireBaseAndroid,
                                            it.length
                                        )

                                        is TdApi.AuthenticationCodeTypeFirebaseIos -> AuthCodeType(
                                            CodeType.FireBaseIos,
                                            it.length
                                        )

                                        else -> AuthCodeType(
                                            CodeType.Other,
                                            manager.getDefaultCodeLength()
                                        )
                                    }
                                }
                                AuthState.WaitCode(
                                    AuthCodeInfo(
                                        type = codeType(codeInfo.type),
                                        nextType = if (codeInfo.nextType != null) codeType(codeInfo.nextType!!) else null,
                                        timeout = codeInfo.timeout
                                    )
                                )
                            }

                            is TdApi.AuthorizationStateWaitPassword -> AuthState.WaitPassword(
                                AuthPasswordInfo(obj.passwordHint)
                            )

                            is TdApi.AuthorizationStateReady -> AuthState.Ready
                            is TdApi.AuthorizationStateLoggingOut -> AuthState.LoggingOut
                            is TdApi.AuthorizationStateClosing -> AuthState.LoggingOut
                            is TdApi.AuthorizationStateClosed -> AuthState.Closed
                            else -> AuthState.Other(obj.toString())
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
            onError = { onError(it.message) }
        )
    }
}