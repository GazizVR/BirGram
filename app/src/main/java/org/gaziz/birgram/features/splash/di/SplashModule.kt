package org.gaziz.birgram.features.splash.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.birgram.features.splash.data.SplashRepoImpl
import org.gaziz.birgram.features.splash.domain.repository.SplashRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class SplashModule {
    @Binds
    abstract fun bindTelegramAuthState(realization: SplashRepoImpl): SplashRepository
}