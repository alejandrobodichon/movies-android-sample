package com.example.moviesexample.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesexample.data.Movie
import com.example.moviesexample.data.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class HomeViewModel(val repository: MoviesRepository): ViewModel(){

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun onUiReady() {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            repository.movies.collect { movies ->
                _state.value = UiState(movies = movies, loading = false)
            }
        }
    }

    data class UiState(
        val loading: Boolean = true,
        val movies: List<Movie> = emptyList()
    )


}