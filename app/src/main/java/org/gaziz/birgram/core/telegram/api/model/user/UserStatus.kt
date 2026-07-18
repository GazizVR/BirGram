package org.gaziz.birgram.core.telegram.api.model.user

import java.time.LocalDateTime

sealed interface UserStatus {
    object Empty: UserStatus
    data class Offline(val lastOnline: LocalDateTime): UserStatus
    data class Online(val expires: LocalDateTime): UserStatus
    data class Recently(val byMyPrivacySettings: Boolean): UserStatus
    object LastWeek: UserStatus
    object LastMonth: UserStatus
}