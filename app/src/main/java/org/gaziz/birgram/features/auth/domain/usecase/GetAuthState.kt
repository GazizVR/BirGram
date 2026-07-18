package org.gaziz.birgram.features.auth.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.internal.ClientManager
import org.gaziz.birgram.core.telegram.internal.updaters.AuthUpdater
import org.gaziz.birgram.core.telegram.api.model.auth.CodeTypeInfo
import org.gaziz.birgram.core.telegram.api.model.auth.AuthCodeInfo
import org.gaziz.birgram.core.telegram.api.model.auth.AuthPasswordInfo
import org.gaziz.birgram.core.telegram.api.model.auth.AuthState
import org.gaziz.birgram.core.telegram.api.model.auth.CodeType
import javax.inject.Inject

class GetAuthState @Inject constructor(
    private val manager: ClientManager,
    private val tgAuth: AuthUpdater
) {
    operator fun invoke(): Flow<AuthState> {
        return tgAuth.authState.map { s ->
            when(s) {
                is TdApi.AuthorizationStateWaitTdlibParameters -> AuthState.WaitParams
                is TdApi.AuthorizationStateWaitPhoneNumber -> AuthState.WaitPhoneNumber
                is TdApi.AuthorizationStateWaitCode -> {
                    val codeInfo = s.codeInfo
                    val codeType: (TdApi.AuthenticationCodeType) -> CodeTypeInfo = {
                        when(it) {
                            is TdApi.AuthenticationCodeTypeCall -> CodeTypeInfo(
                                CodeType.Call,
                                it.length
                            )
                            is TdApi.AuthenticationCodeTypeTelegramMessage -> CodeTypeInfo(
                                CodeType.Telegram,
                                it.length
                            )
                            is TdApi.AuthenticationCodeTypeSms -> CodeTypeInfo(
                                CodeType.SMS,
                                it.length
                            )
                            is TdApi.AuthenticationCodeTypeFlashCall -> CodeTypeInfo(
                                CodeType.FlashCall,
                                manager.getDefaultCodeLength()
                            )
                            is TdApi.AuthenticationCodeTypeMissedCall -> CodeTypeInfo(
                                CodeType.MissedCall,
                                it.length
                            )
                            is TdApi.AuthenticationCodeTypeFragment -> CodeTypeInfo(
                                CodeType.Fragment,
                                it.length
                            )
                            is TdApi.AuthenticationCodeTypeFirebaseAndroid -> CodeTypeInfo(
                                CodeType.FireBaseAndroid,
                                it.length
                            )
                            is TdApi.AuthenticationCodeTypeFirebaseIos -> CodeTypeInfo(
                                CodeType.FireBaseIos,
                                it.length
                            )
                            else -> CodeTypeInfo(
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