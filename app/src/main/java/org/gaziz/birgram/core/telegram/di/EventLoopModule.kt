package org.gaziz.birgram.core.telegram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.birgram.core.telegram.data.TelegramEventLoop
import org.gaziz.birgram.core.telegram.domain.repository.EventLoopRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EventLoopModule {
    @Singleton
    @Binds
    abstract fun bindEventLoop(telegramEventLoop: TelegramEventLoop): EventLoopRepository
}