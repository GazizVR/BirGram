package org.gaziz.birgram.core.telegram.api.model.user

data class User(
    val id: Long,
    val status: UserStatus,
    val type: UserType
)