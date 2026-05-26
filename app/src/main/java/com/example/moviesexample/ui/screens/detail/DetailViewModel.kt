package com.example.moviesexample.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesexample.data.Movie
import com.example.moviesexample.data.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    val id: Int,
    private val repository: MoviesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            repository.findMovieById(id).collect { movie ->
                _state.value = UiState(movie = movie, loading = false)
            }
        }
    }

    fun onFavoriteClick() {
        _state.update { currentState ->
            currentState.copy(
                movie = currentState.movie?.copy(
                    favorite = !currentState.movie.favorite
                ),
            )
        }
        viewModelScope.launch {
            repository.updateMovie(_state.value.movie)
        }
    }

    data class UiState(
        val loading: Boolean = true,
        val movie: Movie? = null,
    )
}