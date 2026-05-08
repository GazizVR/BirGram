package org.gaziz.birgram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.birgram.data.remote.TelegramSearchChats
import org.gaziz.birgram.domain.repository.SearchChatsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchChatsModule {
    @Singleton
    @Binds
    abstract fun buildSearchChats(tgSearchChats: TelegramSearchChats): SearchChatsRepository
}