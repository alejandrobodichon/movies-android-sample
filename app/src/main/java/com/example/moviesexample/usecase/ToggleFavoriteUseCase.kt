package com.example.moviesexample.usecase

import com.example.moviesexample.data.Movie
import com.example.moviesexample.data.MoviesRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(movie: Movie) = moviesRepository.toggleFavorite(movie)
}
