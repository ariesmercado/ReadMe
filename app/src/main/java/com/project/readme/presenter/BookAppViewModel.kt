package com.project.readme.presenter

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.readme.common.Resource
import com.project.readme.data.Book
import com.project.readme.data.BookRepository
import com.project.readme.data.UserProfile
import com.project.readme.data.genarator.LessonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookAppViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository
): ViewModel() {
    private val _lessons = MutableStateFlow<List<Book>>(emptyList())
    val lesson = _lessons.asStateFlow()

    private val _profile = MutableStateFlow<Resource<UserProfile?>>(Resource.Success(savedStateHandle["USER"]))
    val profile = _profile.asStateFlow()

    fun updateUserProfile(name: String,age: Int, grade: Int,selectedAvatar: Int) {
        viewModelScope.launch {
            bookRepository.updateUserProfile(name, age, grade, selectedAvatar)
            _profile.value = Resource.Success(bookRepository.getUserProfile())
        }
    }

    fun loadLessons(context: Context) {
        viewModelScope.launch {
            _lessons.value = LessonUtil.loadLessonsFromAssets(context)
        }
    }
}