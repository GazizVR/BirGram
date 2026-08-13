package org.gaziz.birgram.core.telegram.api.model.user

import org.gaziz.birgram.core.telegram.api.model.media.ChatPhoto

data class User(
    val id: Long,
    val firstName: String,
    val photo: ChatPhoto?,
    val status: UserStatus,
    val type: UserType,
    val accentColorId: Int
)