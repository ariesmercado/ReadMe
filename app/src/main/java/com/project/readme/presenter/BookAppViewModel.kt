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
import kotlinx.coroutines.Job
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

    private val _profile = MutableStateFlow<Resource<UserProfile?>>(Resource.Success(savedStateHandle["USER"]))
    val profile = _profile.asStateFlow()

    val favorites: StateFlow<List<String>> = bookRepository.getFavoriteTitles()
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
            val newList = LessonUtil.loadLessonsFromAssets(context, 1).toMutableList()
            _lessons.value = newList
            listCopy.value = _lessons.value
            Timber.d ("newList: ${listCopy.value}")
        }
    }

    fun loadNextLessons(context: Context) {
        val cp = page.value
        Timber.d("loadNextLessons -> $cp")
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
            Timber.d ("newList: ${listCopy.value}")
        }
    }



    fun search(term: String, context: Context) {
        job?.cancel()
        job = viewModelScope.launch {
            if (term.isNotBlank()) {
                val newList = _lessons.value.filter { it.name.lowercase().contains(term.lowercase()) }.toMutableList()
                if ((newList.size % 2) != 0) {
                    newList.add(Book("++", emptyList()))
                }
                _lessons.value = newList
            } else {
                _lessons.value = listCopy.value
            }
        }
    }
}