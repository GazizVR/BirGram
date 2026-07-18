package org.gaziz.birgram.core.telegram.api.model.auth

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

data class CodeTypeInfo(
    val type: CodeType,
    val length: Int
)

data class AuthCodeInfo(
    val type: CodeTypeInfo,
    val nextType: CodeTypeInfo?,
    val timeout: Int,
)
