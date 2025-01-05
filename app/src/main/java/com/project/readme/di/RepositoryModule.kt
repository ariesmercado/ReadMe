package com.project.readme.di

import com.project.readme.common.DataStorageHelper
import com.project.readme.data.AppDatabase
import com.project.readme.data.BookRepository
import com.project.readme.data.BookRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideBookRepository(
        dataStorageHelper: DataStorageHelper,
        database: AppDatabase,
    ): BookRepository {
        return BookRepositoryImpl(dataStorageHelper, database)
    }
}