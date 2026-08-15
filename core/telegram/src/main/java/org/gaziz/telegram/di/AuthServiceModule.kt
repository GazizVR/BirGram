package org.gaziz.telegram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.telegram.api.AuthService
import org.gaziz.telegram.impl.AuthServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthServiceModule {
    @Binds
    @Singleton
    abstract fun bindAuthService(impl: AuthServiceImpl): AuthService
}