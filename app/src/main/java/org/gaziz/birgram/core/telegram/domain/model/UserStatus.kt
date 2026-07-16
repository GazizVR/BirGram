package org.gaziz.birgram.core.telegram.domain.model

import java.time.LocalDateTime

sealed class UserStatus {
    object Empty: UserStatus()
    data class Offline(val lastOnline: LocalDateTime): UserStatus()
    data class Online(val expires: LocalDateTime): UserStatus()
    object Recently: UserStatus()
    object LastWeek: UserStatus()
    object LastMonth: UserStatus()
}