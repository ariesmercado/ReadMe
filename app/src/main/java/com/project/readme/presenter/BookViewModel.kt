package com.project.readme.presenter

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.readme.data.Book
import com.project.readme.data.genarator.LessonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle): ViewModel() {
    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook: StateFlow<Book?> get() = _selectedBook

    private val _sttResult = MutableStateFlow<Boolean?>(null)
    val sttResult = _sttResult.asStateFlow()

    fun loadLessons(context: Context) {
        viewModelScope.launch {
            _selectedBook.value = LessonUtil.loadLessonsFromAssets(context).find { it.name == savedStateHandle["Title"] }
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
}
