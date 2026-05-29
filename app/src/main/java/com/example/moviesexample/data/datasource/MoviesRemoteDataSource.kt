package com.example.moviesexample.data.datasource

import com.example.moviesexample.framework.remote.RemoteMovie
import com.example.moviesexample.domain.Movie

interface MoviesRemoteDataSource {
    suspend fun fetchPopularMovies(findLastRegion: String): List<Movie>

    suspend fun findMovieById(id: Int): Movie
    fun RemoteMovie.toDomainModel(): Movie
}