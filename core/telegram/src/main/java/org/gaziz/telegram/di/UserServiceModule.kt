package org.gaziz.telegram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.telegram.api.UserService
import org.gaziz.telegram.impl.UserServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserServiceModule {
    @Binds
    @Singleton
    abstract fun bindUserService(impl: UserServiceImpl): UserService
}