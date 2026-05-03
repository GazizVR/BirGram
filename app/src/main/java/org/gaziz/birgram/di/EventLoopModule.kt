package org.gaziz.birgram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.birgram.data.remote.TelegramEventLoop
import org.gaziz.birgram.domain.repository.EventLoopRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class EventLoopModule {
    @Binds
    abstract fun bindEventLoop(telegramEventLoop: TelegramEventLoop): EventLoopRepository
}