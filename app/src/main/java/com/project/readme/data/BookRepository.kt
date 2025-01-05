package com.project.readme.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName
import com.project.readme.common.DataStorageHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize

interface BookRepository {
    val user: StateFlow<UserProfile?>
    suspend fun updateUserProfile(name: String, age:Int, grade: Int, @DrawableRes profilePic: Int)
    suspend fun getUserProfile(): UserProfile?
    suspend fun addToFavorites():Long
    suspend fun removeToFavorites()
    fun getFavorites(): Flow<List<Any>>
}

class BookRepositoryImpl(private val dataStorageHelper: DataStorageHelper): BookRepository {
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

    override suspend fun addToFavorites(): Long {
        TODO("Not yet implemented")
    }

    override suspend fun removeToFavorites() {
        TODO("Not yet implemented")
    }

    override fun getFavorites(): Flow<List<Any>> {
        TODO("Not yet implemented")
    }

}

@Parcelize
data class UserProfile(
    @SerializedName("name") val name: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("grade") val grade: Int? = null,
    @SerializedName("profilePic") val profilePic: Int? = null
): Parcelable