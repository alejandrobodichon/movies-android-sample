package com.example.architectcoders.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architectcoders.data.Movie
import com.example.architectcoders.data.MoviesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel(){

    var state by mutableStateOf(UiState())
    private set

    private val repository = MoviesRepository()

    fun onUiReady(){
        viewModelScope.launch {
            repository.fetchPopularMovies()
        }
    }







    data class UiState(
        val loading: Boolean = false,
        val movies: List<Movie> = emptyList()
    )

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    fun getMovies(){

    }

}