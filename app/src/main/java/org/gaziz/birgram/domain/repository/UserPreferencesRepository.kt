package org.gaziz.birgram.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isDark: Flow<Boolean>
    suspend fun switchIsDark(isDark: Boolean)
    val isRegistered: Flow<Boolean>
    suspend fun switchRegistered(register: Boolean)
}
