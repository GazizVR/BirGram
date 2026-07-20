package org.gaziz.birgram.features.searchChats.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.birgram.features.searchChats.data.ChatSearchRepositoryImpl
import org.gaziz.birgram.features.searchChats.domain.repository.ChatSearchRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatSearchModule {
    @Binds
    @Singleton
    abstract fun bindChatSearch(impl: ChatSearchRepositoryImpl): ChatSearchRepository
}