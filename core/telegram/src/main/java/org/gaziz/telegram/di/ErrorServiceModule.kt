package org.gaziz.telegram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.telegram.api.ErrorService
import org.gaziz.telegram.impl.ErrorServiceImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorServiceModule {
    @Binds
    @Singleton
    abstract fun bindErrorService(impl: ErrorServiceImpl): ErrorService
}