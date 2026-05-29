package com.example.moviesexample.usecase

import com.example.moviesexample.domain.Movie
import com.example.moviesexample.data.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchMoviesUseCase @Inject constructor(
    val repository: MoviesRepository
){
    operator fun invoke(): Flow<List<Movie>> = repository.movies
}