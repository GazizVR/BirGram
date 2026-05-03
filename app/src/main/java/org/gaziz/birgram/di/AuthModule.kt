package org.gaziz.birgram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import org.gaziz.birgram.data.remote.TelegramAuth
import org.gaziz.birgram.domain.repository.AuthRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthModule {
    @Binds
    abstract fun bindTelegramAuth(
        telegramAuth: TelegramAuth
    ): AuthRepository
}