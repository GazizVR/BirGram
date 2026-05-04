package org.gaziz.birgram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.birgram.data.remote.TelegramEventLoop
import org.gaziz.birgram.domain.repository.EventLoopRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EventLoopModule {
    @Singleton
    @Binds
    abstract fun bindEventLoop(telegramEventLoop: TelegramEventLoop): EventLoopRepository
}