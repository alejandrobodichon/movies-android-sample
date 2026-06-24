package com.example.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.extension.stateAsResultIn
import com.example.moviesexample.domain.entity.Movie
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.moviesexample.domain.usecase.FindMovieByIdUseCase
import com.example.moviesexample.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.moviesexample.domain.entity.Result
import com.example.ui.model.MovieUiModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class DetailViewModel @Inject constructor(
    movieId: Int,
    findMovieByIdUseCase: FindMovieByIdUseCase,
    val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val movieFlow = findMovieByIdUseCase(movieId)
        .shareIn(scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    val movieDetail: StateFlow<Result<MovieUiModel>> = movieFlow
        .map { it.toUiModel() }
        .stateAsResultIn(viewModelScope)

    val isFavorite: StateFlow<Boolean> = movieFlow
        .map { it.favorite }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun onFavoriteClick() {
        viewModelScope.launch {
            movieFlow.firstOrNull()?.let { movie ->
                toggleFavoriteUseCase(movie)
            }
        }
    }

    private fun Movie.toUiModel() =
        MovieUiModel(
            id = id,
            poster = poster,
            title = title,
            releaseDate = releaseDate,
            overview = overview,
            backdrop = backdrop,
            originalTitle = originalTitle,
            originalLanguage = originalLanguage,
            popularity = popularity,
            voteAverage = voteAverage
        )

}
