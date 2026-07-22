package com.example.moviesexample.domain.usecase

import com.example.moviesexample.domain.repository.IMoviesRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    val moviesRepository: IMoviesRepository
) {
    suspend operator fun invoke(movieId: Int, isFavorite: Boolean) = moviesRepository.toggleFavorite(movieId, isFavorite)
}
