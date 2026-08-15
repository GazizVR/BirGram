package org.gaziz.telegram.api.model.user

import org.gaziz.telegram.api.model.media.ProfilePhoto

data class User(
    val id: Long,
    val firstName: String,
    val photo: ProfilePhoto?,
    val status: UserStatus,
    val type: UserType,
    val accentColorId: Int
)