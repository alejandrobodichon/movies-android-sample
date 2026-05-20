package com.example.architectcoders.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architectcoders.data.Movie
import com.example.architectcoders.data.MoviesRepository
import kotlinx.coroutines.launch

class DetailViewModel(val id: Int): ViewModel() {

    var state by mutableStateOf(UiState())
        private set

    private val repository = MoviesRepository()

    init {
        viewModelScope.launch {
            state = state.copy(loading = true)
            val movie = repository.findMovieById(id)
            state = state.copy(movie = movie, loading = false)
        }
    }

    data class UiState(
        val loading: Boolean = true,
        val movie: Movie? = null
    )
}