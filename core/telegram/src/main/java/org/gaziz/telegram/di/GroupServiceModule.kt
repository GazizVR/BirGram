package org.gaziz.telegram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.telegram.api.GroupService
import org.gaziz.telegram.impl.GroupServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GroupServiceModule {
    @Binds
    @Singleton
    abstract fun bindGroupService(impl: GroupServiceImpl): GroupService
}