package com.example.moviesexample.domain.datasource

import com.example.moviesexample.domain.entity.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesLocalDataSource {
    val movies: Flow<List<Movie>>
    fun findMovieById(id: Int): Flow<Movie?>

    suspend fun saveMovies(movies: List<Movie>)
    suspend fun updateFavorite(movieId: Int, favorite: Boolean)
}