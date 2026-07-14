package org.gaziz.birgram.features.auth.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import org.gaziz.birgram.features.auth.data.TelegramAuth
import org.gaziz.birgram.features.auth.domain.repository.AuthRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindTelegramAuth(
        telegramAuth: TelegramAuth
    ): AuthRepository
}