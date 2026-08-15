package org.gaziz.telegram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.telegram.api.ChatService
import org.gaziz.telegram.impl.ChatServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatServiceModule {
    @Binds
    @Singleton
    abstract fun bindChatService(impl: ChatServiceImpl): ChatService
}