package org.gaziz.birgram.core.telegram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.birgram.core.telegram.api.MessageService
import org.gaziz.birgram.core.telegram.impl.MessageServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MessageServiceModule {
    @Binds
    @Singleton
    abstract fun bindMessageService(impl: MessageServiceImpl): MessageService
}