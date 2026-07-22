package com.example.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.extension.getOrNull
import com.example.data.extension.mapResult
import com.example.data.extension.stateAsResultIn
import com.example.moviesexample.domain.entity.Movie
import com.example.moviesexample.domain.entity.Result
import com.example.moviesexample.domain.usecase.FindMovieByIdUseCase
import com.example.moviesexample.domain.usecase.ToggleFavoriteUseCase
import com.example.ui.model.MovieUiModel
import com.example.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    val movieId: Int,
    findMovieByIdUseCase: FindMovieByIdUseCase,
    val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val movieFlow: StateFlow<Result<Movie>> =
        findMovieByIdUseCase(movieId)
            .stateAsResultIn(viewModelScope)

    val movieState: StateFlow<Result<MovieUiModel>> = movieFlow
        .map { it.mapResult(Movie::toUiModel) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = Result.Loading
        )

    val favoriteState: StateFlow<Boolean> = movieFlow
        .map { it.getOrNull()?.favorite ?: false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = false
        )

    fun onFavoriteClick() {
        viewModelScope.launch {
            toggleFavoriteUseCase(movieId, favoriteState.value)
        }
    }

    companion object {
        private const val STOP_TIMEOUT_MILLIS = 5000L
    }
}
