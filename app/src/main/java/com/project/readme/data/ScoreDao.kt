package com.project.readme.data

import androidx.room.Dao
import androidx.room.Query
import com.project.readme.common.BaseDao
import com.project.readme.data.entity.Scores
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao : BaseDao<Scores> {

    @Query("SELECT * FROM $TABLE_NAME")
    fun all(): Flow<List<Scores>>

    @Query("SELECT title FROM $TABLE_NAME")
    fun allIds(): Flow<List<String>>

    @Query("SELECT * FROM $TABLE_NAME WHERE title = :title")
    fun get(title: String): Scores?

    @Query("SELECT * FROM $TABLE_NAME ORDER BY title DESC LIMIT 1")
    fun getFirst(): Flow<Scores?>

    @Query("DELETE FROM $TABLE_NAME WHERE title = :title")
    suspend fun deleteByTitle(title: String)

    @Query("DELETE FROM $TABLE_NAME")
    fun nukeTable()

    companion object {
        const val TABLE_NAME = "Scores"
    }
}