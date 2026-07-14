package org.gaziz.birgram.features.chatList.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.birgram.features.chatList.data.TelegramChatList
import org.gaziz.birgram.features.chatList.domain.repository.ChatListRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatListModule {
    @Singleton
    @Binds
    abstract fun bindChatList(telegramChatList: TelegramChatList): ChatListRepository
}