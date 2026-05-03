package org.gaziz.birgram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.birgram.data.local.UserPreferences
import org.gaziz.birgram.domain.repository.UserPreferencesRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class UserPreferencesModule {
    @Binds
    abstract fun bindUserPrefs(userPrefs: UserPreferences): UserPreferencesRepository
}