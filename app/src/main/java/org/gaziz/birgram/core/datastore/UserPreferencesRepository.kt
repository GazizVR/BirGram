package org.gaziz.birgram.core.datastore

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isDark: Flow<Boolean>
    suspend fun switchIsDark(isDark: Boolean)
}