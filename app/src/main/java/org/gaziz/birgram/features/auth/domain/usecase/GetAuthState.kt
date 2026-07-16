package org.gaziz.birgram.features.auth.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.ClientManager
import org.gaziz.birgram.core.telegram.data.source.TelegramAuth
import org.gaziz.birgram.features.auth.domain.model.AuthCode
import org.gaziz.birgram.features.auth.domain.model.AuthCodeInfo
import org.gaziz.birgram.features.auth.domain.model.AuthPasswordInfo
import org.gaziz.birgram.features.auth.domain.model.AuthState
import org.gaziz.birgram.features.auth.domain.model.CodeType
import javax.inject.Inject

class GetAuthState @Inject constructor(
    private val manager: ClientManager,
    private val tgAuth: TelegramAuth
) {
    operator fun invoke(): Flow<AuthState> {
        return tgAuth.authState.map { s ->
            when(s) {
                is TdApi.AuthorizationStateWaitTdlibParameters -> AuthState.WaitParams
                is TdApi.AuthorizationStateWaitPhoneNumber -> AuthState.WaitPhoneNumber
                is TdApi.AuthorizationStateWaitCode -> {
                    val codeInfo = s.codeInfo
                    val codeType: (TdApi.AuthenticationCodeType) -> AuthCode = {
                        when(it) {
                            is TdApi.AuthenticationCodeTypeCall -> AuthCode(
                                CodeType.Call,
                                it.length
                            )
                            is TdApi.AuthenticationCodeTypeTelegramMessage -> AuthCode(
                                CodeType.Telegram,
                                it.length
                            )
                            is TdApi.AuthenticationCodeTypeSms -> AuthCode(
                                CodeType.SMS,
                                it.length
                            )
                            is TdApi.AuthenticationCodeTypeFlashCall -> AuthCode(
                                CodeType.FlashCall,
                                manager.getDefaultCodeLength()
                            )
                            is TdApi.AuthenticationCodeTypeMissedCall -> AuthCode(
                                CodeType.MissedCall,
                                it.length
                            )
                            is TdApi.AuthenticationCodeTypeFragment -> AuthCode(
                                CodeType.Fragment,
                                it.length
                            )
                            is TdApi.AuthenticationCodeTypeFirebaseAndroid -> AuthCode(
                                CodeType.FireBaseAndroid,
                                it.length
                            )
                            is TdApi.AuthenticationCodeTypeFirebaseIos -> AuthCode(
                                CodeType.FireBaseIos,
                                it.length
                            )
                            else -> AuthCode(
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
                    AuthPasswordInfo(s.passwordHint)
                )
                is TdApi.AuthorizationStateReady -> AuthState.Ready

                is TdApi.AuthorizationStateLoggingOut -> AuthState.LoggingOut
                is TdApi.AuthorizationStateClosing -> AuthState.LoggingOut
                is TdApi.AuthorizationStateClosed -> AuthState.Closed

                else -> AuthState.Other(s.toString())
            }
        }
    }
}