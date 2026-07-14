package org.gaziz.birgram.features.searchChats.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.birgram.features.searchChats.data.TelegramSearchChats
import org.gaziz.birgram.features.searchChats.domain.repository.SearchChatsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchChatsModule {
    @Singleton
    @Binds
    abstract fun buildSearchChats(tgSearchChats: TelegramSearchChats): SearchChatsRepository
}