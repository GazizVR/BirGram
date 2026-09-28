package org.gaziz.birgram.core.telegram.ui.model

import org.gaziz.birgram.core.telegram.api.model.user.UserStatus

interface ChatTypeUiState {
    data class User(
        val isBot: Boolean,
        val status: UserStatus
    ): ChatTypeUiState
    data class BasicGroup(
        val memberCount: Int
    ): ChatTypeUiState
    data class SuperGroup(
        val memberCount: Int,
        val isChannel: Boolean
    ): ChatTypeUiState
}