package com.example.moviesexample.domain.usecase

import com.example.moviesexample.domain.entity.Movie
import com.example.moviesexample.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchMoviesUseCase @Inject constructor(
    val repository: IMoviesRepository
){
    operator fun invoke(): Flow<List<Movie>> = repository.movies
}