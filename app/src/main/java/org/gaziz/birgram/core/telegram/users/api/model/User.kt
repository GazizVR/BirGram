package org.gaziz.birgram.core.telegram.users.api.model

data class User(
    val id: Long,
    val status: UserStatus,
    val type: UserType
)