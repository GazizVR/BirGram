package org.gaziz.birgram.core.ui.model

import org.gaziz.telegram.api.model.user.UserStatus

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