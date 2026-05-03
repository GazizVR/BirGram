package org.gaziz.birgram.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.gaziz.birgram.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class GetIsDarkTheme @Inject constructor(
    private val repo: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> = repo.isDark
}