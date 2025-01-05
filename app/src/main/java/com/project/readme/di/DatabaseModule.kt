package com.project.readme.di

import android.content.Context
import androidx.room.Room
import com.project.readme.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "ReadMeDB"
        ).build()
    }

    @Provides
    fun provideFavoriteDao(database: AppDatabase) = database.favoriteDao()
}