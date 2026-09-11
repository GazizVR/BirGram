package org.gaziz.searchchats.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.searchchats.data.ChatSearchRepositoryImpl
import org.gaziz.searchchats.domain.repository.ChatSearchRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatSearchModule {
    @Binds
    @Singleton
    abstract fun bindChatSearch(impl: ChatSearchRepositoryImpl): ChatSearchRepository
}