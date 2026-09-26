package org.gaziz.birgram.core.telegram.internal.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.model.auth.AuthCodeInfo
import org.gaziz.birgram.core.telegram.api.model.auth.AuthPasswordInfo
import org.gaziz.birgram.core.telegram.api.model.auth.AuthState
import org.gaziz.birgram.core.telegram.api.model.auth.CodeType
import org.gaziz.birgram.core.telegram.api.model.auth.CodeTypeInfo

fun TdApi.AuthenticationCodeType.toCodeTypeInfo(defaultCodeLength: Int): CodeTypeInfo {
    return when(this) {
        is TdApi.AuthenticationCodeTypeCall -> CodeTypeInfo(CodeType.Call,this.length)
        is TdApi.AuthenticationCodeTypeTelegramMessage -> CodeTypeInfo(CodeType.Telegram,this.length)
        is TdApi.AuthenticationCodeTypeSms -> CodeTypeInfo(CodeType.SMS,this.length)
        is TdApi.AuthenticationCodeTypeFlashCall -> CodeTypeInfo(CodeType.FlashCall,defaultCodeLength)
        is TdApi.AuthenticationCodeTypeMissedCall -> CodeTypeInfo(CodeType.MissedCall,this.length)
        is TdApi.AuthenticationCodeTypeFragment -> CodeTypeInfo(CodeType.Fragment,this.length)
        is TdApi.AuthenticationCodeTypeFirebaseAndroid -> CodeTypeInfo(CodeType.FireBaseAndroid,this.length)
        is TdApi.AuthenticationCodeTypeFirebaseIos -> CodeTypeInfo(CodeType.FireBaseIos,this.length)
        else -> CodeTypeInfo(CodeType.Other,defaultCodeLength)
    }
}

fun TdApi.AuthorizationState.toAuthState(defaultCodeLength: Int): AuthState {
    return when(this) {
        is TdApi.AuthorizationStateWaitTdlibParameters -> AuthState.WaitParams
        is TdApi.AuthorizationStateWaitPhoneNumber -> AuthState.WaitPhoneNumber
        is TdApi.AuthorizationStateWaitCode -> {
            val codeInfo = this.codeInfo
            val codeType = codeInfo.type.toCodeTypeInfo(defaultCodeLength)
            AuthState.WaitCode(
                AuthCodeInfo(
                    type = codeType,
                    nextType = if (codeInfo.nextType != null) codeType else null,
                    timeout = codeInfo.timeout
                )
            )
        }
        is TdApi.AuthorizationStateWaitPassword -> AuthState.WaitPassword(
            AuthPasswordInfo(this.passwordHint)
        )
        is TdApi.AuthorizationStateReady -> AuthState.Ready

        is TdApi.AuthorizationStateLoggingOut -> AuthState.LoggingOut
        is TdApi.AuthorizationStateClosing -> AuthState.LoggingOut
        is TdApi.AuthorizationStateClosed -> AuthState.Closed

        else -> AuthState.Other(this.toString())
    }
}