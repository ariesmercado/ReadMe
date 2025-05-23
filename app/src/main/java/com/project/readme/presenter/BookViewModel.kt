package com.project.readme.presenter

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.readme.data.Book
import com.project.readme.data.BookRepository
import com.project.readme.data.UserProfile
import com.project.readme.data.genarator.LessonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle, private val bookRepository: BookRepository): ViewModel() {
    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook: StateFlow<Book?> get() = _selectedBook

    val favorites: StateFlow<List<String>> = bookRepository.getFavoriteTitles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _sttResult = MutableStateFlow<Boolean?>(null)
    val sttResult = _sttResult.asStateFlow()

    fun loadLessons(context: Context) {
        viewModelScope.launch {
            delay(1500)
            savedStateHandle.get<String?>("Title")?.let {
                _selectedBook.value = LessonUtil.loadBookFromAssets(context, it)
            }
        }
    }

    fun exitBook() {
        _selectedBook.value = null
    }

    fun updateSttResult(result: Boolean) {
        _sttResult.value = result
    }

    fun shutDownStt() {
        _sttResult.value = null
    }

    fun toggleFavorite(title: String, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                if (isFavorite) {
                    bookRepository.removeToFavorites(title)
                } else {
                    bookRepository.addToFavorites(title)
                }
            } catch (_: Exception) {}

        }
    }
}
