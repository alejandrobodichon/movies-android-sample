package com.example.moviesexample.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesexample.data.Movie
import com.example.moviesexample.data.MoviesRepository
import com.example.moviesexample.stateAsResultIn
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.moviesexample.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.Serializable
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    movieId: Int,
    private val repository: MoviesRepository
) : ViewModel() {

    val state: StateFlow<Result<Movie>> = repository.findMovieById(movieId)
        .stateAsResultIn(scope = viewModelScope)

    fun onFavoriteClick() {
        val currentState = state.value
        if (currentState is Result.Success<Movie>) {
            viewModelScope.launch {
                repository.toggleFavorite((currentState.data))
            }
        }
    }

}