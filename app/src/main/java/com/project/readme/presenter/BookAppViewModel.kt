package com.project.readme.presenter

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.readme.common.Resource
import com.project.readme.data.Book
import com.project.readme.data.BookRepository
import com.project.readme.data.UserProfile
import com.project.readme.data.entity.Scores
import com.project.readme.data.genarator.LessonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BookAppViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository
): ViewModel() {
    private val _lessons = MutableStateFlow<List<Book>>(emptyList())
    val lesson = _lessons.asStateFlow()

    private val _firstLessons = MutableStateFlow<List<Book>>(emptyList())
    val firstLessons = _firstLessons.asStateFlow()

    private val _secondLessons = MutableStateFlow<List<Book>>(emptyList())
    val secondLessons = _secondLessons.asStateFlow()

    private val _profile = MutableStateFlow<Resource<UserProfile?>>(Resource.Success(savedStateHandle["USER"]))
    val profile = _profile.asStateFlow()

    val favorites: StateFlow<List<String>> = bookRepository.getFavoriteTitles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val scores: StateFlow<List<Scores>> = bookRepository.getScores()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val listCopy = MutableStateFlow<List<Book>>(emptyList())

    private val _page = MutableStateFlow(1)
    val page = _page.asStateFlow()

    private var job: Job? = null

    fun updateUserProfile(name: String,age: Int, grade: Int,selectedAvatar: Int) {
        viewModelScope.launch {
            bookRepository.updateUserProfile(name, age, grade, selectedAvatar)
            _profile.value = Resource.Success(bookRepository.getUserProfile())
        }
    }

    fun loadLessons(context: Context) {
        viewModelScope.launch {
            coroutineScope {
                val firstLessonsDeferred = async {
                    LessonUtil.loadFirstLessonsFromAssets(context).toMutableList()
                }
                val secondLessonsDeferred = async {
                    LessonUtil.loadSecondLessonsFromAssets(context).toMutableList()
                }
                val lessonsDeferred = async {
                    LessonUtil.loadLessonsFromAssets(context, 1).toMutableList()
                }

                _firstLessons.value = firstLessonsDeferred.await()
                _secondLessons.value = secondLessonsDeferred.await()
                val newList = lessonsDeferred.await()
                _lessons.value = newList
                listCopy.value = newList
            }
        }
    }


    fun loadNextLessons(context: Context) {
        val cp = page.value
        if (cp > 2) return
        viewModelScope.launch {
            _page.value = cp + 1
            val newList = LessonUtil.loadLessonsFromAssets(context, _page.value).toMutableList()
            if ((newList.size % 2) != 0 && _page.value == 3) {
                newList.add(Book("++", emptyList()))
            }

            val transList: MutableList<Book> = _lessons.value.toMutableList()
            transList.addAll(newList)
            _lessons.value = transList
            listCopy.value = _lessons.value
        }
    }

}