package org.gaziz.birgram.features.chat.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.birgram.features.chat.data.TelegramChat
import org.gaziz.birgram.features.chat.domain.repository.ChatRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {
    @Binds
    @Singleton
    abstract fun bindChat(tgChat: TelegramChat): ChatRepository
}