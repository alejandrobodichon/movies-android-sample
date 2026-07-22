package com.example.moviesexample.domain.datasource

import com.example.moviesexample.domain.entity.Movie

interface MoviesRemoteDataSource {
    suspend fun fetchPopularMovies(findLastRegion: String): List<Movie>

    suspend fun findMovieById(id: Int): Movie
}
