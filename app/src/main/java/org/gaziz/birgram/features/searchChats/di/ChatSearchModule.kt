package org.gaziz.birgram.features.searchChats.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import org.gaziz.birgram.features.searchChats.data.ChatSearchRepositoryImpl
import org.gaziz.birgram.features.searchChats.domain.repository.ChatSearchRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class ChatSearchModule {
    @Binds
    abstract fun bindChatSearch(impl: ChatSearchRepositoryImpl): ChatSearchRepository
}