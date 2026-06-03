package com.example.moviesexample.domain.usecase

import com.example.moviesexample.domain.repository.IMoviesRepository
import javax.inject.Inject

class FindMovieByIdUseCase @Inject constructor(
    val moviesRepository: IMoviesRepository
) {
    operator fun invoke(id: Int) = moviesRepository.findMovieById(id)
}
