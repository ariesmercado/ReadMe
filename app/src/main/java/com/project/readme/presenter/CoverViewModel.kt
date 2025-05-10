package com.project.readme.presenter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.readme.common.Resource
import com.project.readme.data.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoverViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val stateHandle: SavedStateHandle
): ViewModel() {

    private val _score = MutableStateFlow<Resource<Int?>>(Resource.Loading())
    val score = _score.asStateFlow()

    val level = stateHandle.get<String>("level")

    init {
        getScore()
    }

    fun getScore() {
        viewModelScope.launch {
            _score.value = Resource.Success(bookRepository.getScore(level!!))
        }
    }
}