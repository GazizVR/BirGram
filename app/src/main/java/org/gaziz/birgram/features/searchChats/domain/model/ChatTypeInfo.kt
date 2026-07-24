package org.gaziz.birgram.features.searchChats.domain.model

import org.gaziz.birgram.core.telegram.api.model.user.UserStatus

interface ChatTypeInfo {
    data class User(
        val isBot: Boolean,
        val status: UserStatus
    ): ChatTypeInfo
    data class BasicGroup(
        val memberCount: Int
    ): ChatTypeInfo
    data class SuperGroup(
        val memberCount: Int,
        val isChannel: Boolean
    ): ChatTypeInfo
}