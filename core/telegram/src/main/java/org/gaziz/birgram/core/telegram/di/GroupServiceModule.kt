package org.gaziz.birgram.core.telegram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.birgram.core.telegram.api.GroupService
import org.gaziz.birgram.core.telegram.impl.GroupServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GroupServiceModule {
    @Binds
    @Singleton
    abstract fun bindGroupService(impl: GroupServiceImpl): GroupService
}