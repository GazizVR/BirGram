package org.gaziz.birgram.core.telegram.auth.api.model

enum class CodeType {
    Telegram,
    SMS,
    Call,
    FlashCall,
    MissedCall,
    Fragment,
    FireBaseAndroid,
    FireBaseIos,
    Other
}

data class AuthCode(
    val type: CodeType,
    val length: Int
)

data class AuthCodeInfo(
    val type: AuthCode,
    val nextType: AuthCode?,
    val timeout: Int,
)
