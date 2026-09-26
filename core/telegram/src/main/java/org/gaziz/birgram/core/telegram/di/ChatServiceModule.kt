package org.gaziz.birgram.core.telegram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.impl.ChatServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatServiceModule {
    @Binds
    @Singleton
    abstract fun bindChatService(impl: ChatServiceImpl): ChatService
}