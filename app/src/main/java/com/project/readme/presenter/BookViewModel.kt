package com.project.readme.presenter

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.readme.data.Book
import com.project.readme.data.BookRepository
import com.project.readme.data.Quiz
import com.project.readme.data.Quizzes
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

    private val _quizes = MutableStateFlow<Quizzes?>(null)
    val quizes: StateFlow<Quizzes?> get() = _quizes

    private val _currentQuiz = MutableStateFlow<Quiz?>(null)
    val currentQuiz: StateFlow<Quiz?> get() = _currentQuiz

    private val _currentQuizIndex = MutableStateFlow<Int?>(null)
    val currentQuizIndex: StateFlow<Int?> get() = _currentQuizIndex

    private val _correctAnswer = MutableStateFlow<Int>(0)
    val correctAnswer: StateFlow<Int> get() = _correctAnswer

    private val _selectedAnswer = MutableStateFlow<Int?>(null)
    val selectedAnswer: StateFlow<Int?> get() = _selectedAnswer

    val favorites: StateFlow<List<String>> = bookRepository.getFavoriteTitles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _sttResult = MutableStateFlow<Boolean?>(null)
    val sttResult = _sttResult.asStateFlow()

    private val _quizStatus = MutableStateFlow<QuizStatus>(QuizStatus.Hide)
    val quizStatus = _quizStatus.asStateFlow()

    fun loadLessons(context: Context) {
        viewModelScope.launch {
            delay(1500)
            savedStateHandle.get<String?>("Title")?.let {
                _selectedBook.value = LessonUtil.loadBookFromAssets(context, it)
                val term = if(it.contains(":")) { it.split(":")[0] } else { it }
                _quizes.value = LessonUtil.getQuizzes(term)
                if (_quizes.value != null) {
                    _currentQuiz.value = _quizes.value?.quiz?.first()
                    _currentQuizIndex.value = 0
                }
                _correctAnswer.value = 0
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

    fun updateStatus(qs: QuizStatus) {
        viewModelScope.launch {
            _quizStatus.value = qs
        }
    }

    fun onChooseAnswer(answer: Int) {
        _selectedAnswer.value = answer
    }

    fun onNextQuiz(answer: Int) {
        if (_currentQuiz.value?.correctAnswer == answer) {
            _correctAnswer.value = _correctAnswer.value + 1
        }

        _currentQuizIndex.value?.let {
            val cqi = _currentQuizIndex.value
            _currentQuizIndex.value = (cqi ?: 0) + 1
            _currentQuiz.value =  quizes.value?.quiz?.get(_currentQuizIndex.value ?: 0)
            _selectedAnswer.value = null
        }
    }

    fun onSubmitQuiz(answer: Int) {
        viewModelScope.launch {

            if (_currentQuiz.value?.correctAnswer == answer ) {
                _correctAnswer.value = _correctAnswer.value + 1
            }

            updateStatus(QuizStatus.Result)
            _currentQuizIndex.value?.let {
                _currentQuizIndex.value = 0
                _currentQuiz.value =  quizes.value?.quiz?.get(0)
                _selectedAnswer.value = null
            }

            bookRepository.addToScore(_selectedBook.value?.name.orEmpty(), correctAnswer.value)
        }

    }

    fun onForceSubmitScore() {
        viewModelScope.launch {
            bookRepository.addToScore(_selectedBook.value?.name.orEmpty(), 3)
        }
    }
}

sealed class QuizStatus() {
    data object Show: QuizStatus()
    data object Hide: QuizStatus()
    data object Result: QuizStatus()
}
