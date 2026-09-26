package org.gaziz.birgram.core.telegram.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.gaziz.birgram.core.telegram.api.FileService
import org.gaziz.birgram.core.telegram.impl.FileServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FileServiceModule {
    @Binds
    @Singleton
    abstract fun bindFileService(impl: FileServiceImpl): FileService
}