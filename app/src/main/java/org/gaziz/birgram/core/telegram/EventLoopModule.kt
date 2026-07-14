package org.gaziz.birgram.core.telegram

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EventLoopModule {
    @Singleton
    @Binds
    abstract fun bindEventLoop(telegramEventLoop: TelegramEventLoop): EventLoopRepository
}