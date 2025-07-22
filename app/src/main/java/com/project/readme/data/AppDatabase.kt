package com.project.readme.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.readme.data.entity.Favorites
import com.project.readme.data.entity.Scores

const val VERSION_NUMBER = 2

@Database(
    entities = [Favorites::class, Scores::class],
    version = VERSION_NUMBER,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun scoreDao(): ScoreDao
}