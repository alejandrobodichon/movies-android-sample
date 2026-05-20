package com.example.architectcoders.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architectcoders.data.Movie
import com.example.architectcoders.data.RemoteMovie
import com.example.architectcoders.data.MoviesRepository
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel(){

    var state by mutableStateOf(UiState())
    private set

    private val repository = MoviesRepository()

    fun onUiReady(region: String) {
        viewModelScope.launch {
            val movies = repository.fetchPopularMovies(region)
            state = state.copy(movies = movies, loading = false)
        }
    }

    data class UiState(
        val loading: Boolean = true,
        val movies: List<Movie> = emptyList()
    )


}