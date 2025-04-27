package com.project.readme.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.readme.common.Resource
import com.project.readme.data.BookRepository
import com.project.readme.data.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamResultViewModel @Inject constructor(private val bookRepository: BookRepository): ViewModel() {

    private val _score = MutableStateFlow<Resource<Int?>>(Resource.Loading())
    val score = _score.asStateFlow()

    private val _profile = MutableStateFlow<Resource<UserProfile?>>(Resource.Loading())
    val profile = _profile.asStateFlow()

    init {
        getScore()
        getUserProfile()
    }

    private fun getScore() {
        viewModelScope.launch {
            _score.value = Resource.Success(bookRepository.getScore())
        }
    }

    private fun getUserProfile() {
        viewModelScope.launch {
            _profile.value = Resource.Success(bookRepository.getUserProfile())
        }
    }
}