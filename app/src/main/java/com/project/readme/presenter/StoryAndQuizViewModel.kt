package com.project.readme.presenter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.readme.data.BookRepository
import com.project.readme.data.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryAndQuizViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    val stateHandle: SavedStateHandle
): ViewModel() {

    val level = stateHandle.get<String>("level")

    private val _currentStory = MutableStateFlow(
        when (level) {
            "easy" -> Story.STORY1
            "medium" -> Story.STORY8
            "hard" -> Story.STORY16
            else -> Story.STORY1 // fallback/default
        }
    )
    val currentStory = _currentStory.asStateFlow()

    private val _currentQuiz = MutableStateFlow(0)
    val currentQuiz = _currentQuiz.asStateFlow()

    private val _answerStatus = MutableStateFlow<AnswerStatus>(AnswerStatus.None)
    val answerStatus = _answerStatus.asStateFlow()

    private val _answer = MutableStateFlow<Int?>(null)
    val answer = _answer.asStateFlow()

    private val _score = MutableStateFlow<Int?>(0)
    val score = _score.asStateFlow()

    private fun onNextStory() {
        val nextStory = currentStory.value.nextStory
        nextStory ?: return
        _currentStory.value = nextStory
        onResetStoryBoard()
    }

    fun onNextQuiz() {
        val quiz = currentQuiz.value
        if (quiz == 2) {
            onNextStory()
        }
        else {
            _currentQuiz.value = quiz + 1
            onResetStoryAnswer()
        }
    }

    fun onChooseAnswer(number: Int?) {
        _answer.value = number
    }

    fun onSubmitAnswer(fillBlankAnswer: String? = null) {
        viewModelScope.launch {
            val quizzes = currentStory.value.quiz
            val cQuiz = quizzes[currentQuiz.value]
            val currentScore = score.value
            if(level == "hard" && fillBlankAnswer?.lowercase() == cQuiz.choices[cQuiz.correctAnswer - 1].lowercase()) {
                _score.value = (currentScore ?: 0) + 1
                _answerStatus.value = AnswerStatus.Correct
            }
            else if (cQuiz.correctAnswer == answer.value) {
                _score.value = (currentScore ?: 0) + 1
                _answerStatus.value = AnswerStatus.Correct
            }
            else {
                _answerStatus.value = AnswerStatus.Wrong
            }

            when (level) {
                "easy" -> {
                    if (cQuiz.id == 21) {
                        score.value?.let { bookRepository.updateScore(it, level) }
                    }
                }
                "medium" -> {
                    if (cQuiz.id == 45) {
                        score.value?.let { bookRepository.updateScore(it, level) }
                    }
                }
                "hard" -> {
                    if (cQuiz.id == 69) {
                        score.value?.let { bookRepository.updateScore(it, level) }
                    }
                }
            }
        }
    }

    private fun onResetStoryBoard() {
        _currentQuiz.value = 0
        _answerStatus.value = AnswerStatus.None
        _answer.value = null
    }

    private fun onResetStoryAnswer() {
        _answerStatus.value = AnswerStatus.None
        _answer.value = null
    }

}

sealed class AnswerStatus {
    data object None: AnswerStatus()
    data object Correct: AnswerStatus()
    data object Wrong: AnswerStatus()
}