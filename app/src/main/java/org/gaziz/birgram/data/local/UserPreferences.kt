package org.gaziz.birgram.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.domain.repository.UserPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    @param:ApplicationContext private val context: Context
): UserPreferencesRepository {
    val Context.dataStore by preferencesDataStore("userPreferences")
    private companion object {
        val IS_DARK = booleanPreferencesKey("is_dark")
    }

    private val dataStore = context.dataStore

    override val isDark: Flow<Boolean> = dataStore.data.map {
        it[IS_DARK] ?: true
    }

    override suspend fun switchIsDark(isDark: Boolean) {
        dataStore.updateData {
            it.toMutablePreferences().also { pref ->
                pref[IS_DARK] = isDark
            }
        }
    }

}