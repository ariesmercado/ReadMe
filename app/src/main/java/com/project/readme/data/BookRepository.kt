package com.project.readme.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName
import com.project.readme.common.DataStorageHelper
import com.project.readme.data.entity.Favorites
import com.project.readme.data.entity.Scores
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize

interface BookRepository {
    val user: StateFlow<UserProfile?>
    suspend fun updateUserProfile(name: String, age:Int, grade: Int, @DrawableRes profilePic: Int)
    suspend fun getUserProfile(): UserProfile?
    suspend fun addToFavorites(title: String):Long
    suspend fun addToScore(title: String, score: Int):Long
    suspend fun removeToScore(title: String)
    suspend fun removeToFavorites(title: String)
    fun getScores():Flow<List<Scores>>
    fun getFavorites(): Flow<List<Any>>
    fun getFavoriteTitles(): Flow<List<String>>
    suspend fun updateScore(score: Int, level: String)
    suspend fun getScore(level: String): Int?
}

class BookRepositoryImpl(private val dataStorageHelper: DataStorageHelper, private val database: AppDatabase): BookRepository {
    override val user = MutableStateFlow<UserProfile?>(null)

    override suspend fun updateUserProfile(name: String, age: Int, grade: Int, @DrawableRes profilePic: Int) {
        dataStorageHelper.apply {
            saveValue("UserName", name)
            saveValue("UserAge", age)
            saveValue("UserGrade", grade)
            saveValue("UserProfile", profilePic)
        }

        user.emit(UserProfile(name,age,grade,profilePic))
    }



    override suspend fun getUserProfile(): UserProfile? {
        val userName = dataStorageHelper.getValue<String>("UserName") ?: return null
        val age = dataStorageHelper.getValue<Int>("UserAge") ?: return null
        val grade = dataStorageHelper.getValue<Int>("UserGrade") ?: return null
        val profile = dataStorageHelper.getValue<Int>("UserProfile") ?: return null

        user.emit(UserProfile(userName,age,grade,profile))
        return UserProfile(userName,age, grade, profile)
    }

    override suspend fun addToFavorites(title: String): Long {
        return database.favoriteDao().insert(Favorites(title = title))
    }

    override suspend fun addToScore(title: String,score: Int): Long {
        return database.scoreDao().insert(Scores(title = title, score = score))
    }

    override suspend fun removeToScore(title: String) {
        return database.favoriteDao().deleteByTitle(title)
    }

    override fun getScores(): Flow<List<Scores>> {
        return database.scoreDao().all()
    }

    override suspend fun removeToFavorites(title: String) {
        return database.favoriteDao().deleteByTitle(title)
    }

    override fun getFavoriteTitles(): Flow<List<String>> = database.favoriteDao().allIds()

    override suspend fun updateScore(score: Int, level: String) {
        dataStorageHelper.apply {
            saveValue("Score_$level", score)
        }
    }

    override suspend fun getScore(level: String): Int? {
        return dataStorageHelper.getValue<Int?>("Score_$level")
    }

    override fun getFavorites(): Flow<List<Favorites>> = database.favoriteDao().all()

}

@Parcelize
data class UserProfile(
    @SerializedName("name") val name: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("grade") val grade: Int? = null,
    @SerializedName("profilePic") val profilePic: Int? = null
): Parcelable