package com.example.moviesexample.usecase

import com.example.moviesexample.data.MoviesRepository
import javax.inject.Inject

class FindMovieByIdUseCase @Inject constructor(
    val moviesRepository: MoviesRepository
) {
    operator fun invoke(id: Int) = moviesRepository.findMovieById(id)
}
