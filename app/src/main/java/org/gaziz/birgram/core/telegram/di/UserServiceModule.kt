package org.gaziz.birgram.core.telegram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.impl.UserServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserServiceModule {
    @Binds
    @Singleton
    abstract fun bindUserService(impl: UserServiceImpl): UserService
}