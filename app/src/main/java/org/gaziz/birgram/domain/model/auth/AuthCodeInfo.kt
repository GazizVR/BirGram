package org.gaziz.birgram.domain.model.auth

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

data class AuthCodeType(
    val type: CodeType,
    val length: Int
)

data class AuthCodeInfo(
    val type: AuthCodeType,
    val nextType: AuthCodeType?,
    val timeout: Int,
)
