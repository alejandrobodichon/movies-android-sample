package com.example.architectcoders.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architectcoders.data.Movie
import com.example.architectcoders.data.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(val id: Int): ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val repository = MoviesRepository()

    init {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            _state.value = UiState(movie = repository.findMovieById(id), loading = false)
        }
    }

    data class UiState(
        val loading: Boolean = true,
        val movie: Movie? = null
    )
}