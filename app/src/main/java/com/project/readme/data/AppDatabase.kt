package com.project.readme.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.readme.data.entity.Favorites

const val VERSION_NUMBER = 1

@Database(
    entities = [Favorites::class],
    version = VERSION_NUMBER,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}