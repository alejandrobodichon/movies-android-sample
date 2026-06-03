package com.example.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesexample.domain.entity.Movie
import com.example.moviesexample.domain.entity.stateAsResultIn
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.moviesexample.domain.usecase.FindMovieByIdUseCase
import com.example.moviesexample.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.moviesexample.domain.entity.Result

@HiltViewModel
class DetailViewModel @Inject constructor(
    movieId: Int,
    findMovieByIdUseCase: FindMovieByIdUseCase,
    val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    val state: StateFlow<Result<Movie>> = findMovieByIdUseCase(movieId)
        .stateAsResultIn(scope = viewModelScope)

    fun onFavoriteClick() {
        val currentState = state.value
        if (currentState is Result.Success<Movie>) {
            viewModelScope.launch {
                toggleFavoriteUseCase(currentState.data)
            }
        }
    }

}