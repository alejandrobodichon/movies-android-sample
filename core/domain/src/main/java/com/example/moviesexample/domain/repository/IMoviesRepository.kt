package com.example.moviesexample.domain.repository

import com.example.moviesexample.domain.entity.Movie
import kotlinx.coroutines.flow.Flow
import com.example.moviesexample.domain.entity.Result

interface IMoviesRepository {
    val movies: Flow<List<Movie>>
    fun findMovieById(id: Int): Flow<Movie>
    suspend fun toggleFavorite(movieId: Int, isFavorite: Boolean)
    suspend fun refresh(): Result<Unit>
}