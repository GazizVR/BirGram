package org.gaziz.birgram.features.auth.data

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.TelegramManager
import org.gaziz.birgram.features.auth.domain.model.AuthCodeInfo
import org.gaziz.birgram.features.auth.domain.model.AuthCodeType
import org.gaziz.birgram.features.auth.domain.model.AuthPasswordInfo
import org.gaziz.birgram.features.auth.domain.model.AuthState
import org.gaziz.birgram.features.auth.domain.model.CodeType
import org.gaziz.birgram.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class TelegramAuth @Inject constructor(
    private val manager: TelegramManager
): AuthRepository {

    private val _authState = MutableStateFlow<AuthState>(AuthState.WaitParams)
    override val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    override val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    override fun setErrorMessage(msg: String?) {
        _errorMessage.update { msg }
    }

    override suspend fun collectUpdates(){
        manager.update.collect { u ->
            when(u) {
                is TdApi.Error -> {
                    Log.e(manager.getLogTag(), "${u.code}: ${u.message}")
                    setErrorMessage(u.message)
                }
                is TdApi.Ok -> {
                    setErrorMessage(null)
                }
                is TdApi.UpdateAuthorizationState -> {
                    _authState.value = when (u.authorizationState) {
                        is TdApi.AuthorizationStateWaitTdlibParameters -> AuthState.WaitParams
                        is TdApi.AuthorizationStateWaitPhoneNumber -> AuthState.WaitPhoneNumber
                        is TdApi.AuthorizationStateWaitCode -> {
                            val codeInfo = (u.authorizationState as TdApi.AuthorizationStateWaitCode).codeInfo
                            val codeType: (TdApi.AuthenticationCodeType) -> AuthCodeType = {
                                when(it) {
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
                            AuthPasswordInfo(
                                (u.authorizationState as TdApi.AuthorizationStateWaitPassword).passwordHint
                            )
                        )
                        is TdApi.AuthorizationStateReady -> AuthState.Ready

                        is TdApi.AuthorizationStateLoggingOut -> AuthState.LoggingOut
                        is TdApi.AuthorizationStateClosing -> AuthState.LoggingOut
                        is TdApi.AuthorizationStateClosed -> AuthState.Closed

                        else -> AuthState.Other(u.authorizationState.toString())
                    }
                }
            }
        }
    }

    override suspend fun collectExceptions() {
        manager.exception.collect { e ->
            if(e != null) {
                val message = e.localizedMessage ?: e.message ?: "unknown update handler exception"
                Log.e(manager.getLogTag(), message)
                setErrorMessage(message)
            }
        }
    }

    override fun setPhoneNumber(
        phoneNumber: String,
        onError: (String) -> Unit
    ) {
        val phoneNumber = TdApi.SetAuthenticationPhoneNumber().apply {
            this.phoneNumber = phoneNumber
            this.settings = null
        }
        manager.sendRequest(phoneNumber,{ onError(it.message) })
    }

    override fun checkCode(
        code: String,
        onError: (String) -> Unit
    ) {
        val code = TdApi.CheckAuthenticationCode().apply {
            this.code = code
        }
        manager.sendRequest(code,{ onError(it.message) })
    }

    override fun resendCode(
        isUser: Boolean,
        onError: (String) -> Unit
    ) {
        val parameters = TdApi.ResendAuthenticationCode().apply {
            reason = if (isUser) TdApi.ResendCodeReasonUserRequest()
            else TdApi.ResendCodeReasonVerificationFailed()
        }
        manager.sendRequest(parameters,{ onError(it.message) })
    }

    override fun checkPassword(
        password: String,
        onError: (String) -> Unit
    ) {
        val parameters = TdApi.CheckAuthenticationPassword().apply {
            this.password = password
        }
        manager.sendRequest(parameters,{ onError(it.message) })
    }

    override fun restartAuth() {
        _authState.value = AuthState.WaitPhoneNumber
    }

}