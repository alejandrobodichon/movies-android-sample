package com.example.moviesexample.data.datasource

import com.example.moviesexample.data.Movie
import com.example.moviesexample.data.remote.MoviesClient
import com.example.moviesexample.data.remote.MoviesService
import com.example.moviesexample.data.remote.RemoteMovie
import javax.inject.Inject

class MoviesRemoteDataSource @Inject constructor(
    private val moviesService: MoviesService) {

    suspend fun fetchPopularMovies(findLastRegion: String): List<Movie> {
        val movies = moviesService
            .getPopularMovies(findLastRegion)
            .results
            .map{it.toDomainModel()}
        return movies
    }

    suspend fun findMovieById(id: Int): Movie {
        val movie = moviesService
            .fetchMovieById(id)
            .toDomainModel()
        return movie
    }

    private fun RemoteMovie.toDomainModel() = Movie(
        poster = "https://image.tmdb.org/t/p/w500$posterPath",
        title = title,
        id = id,
        releaseDate = releaseDate,
        backdrop = backdropPath?.let{"https://image.tmdb.org/t/p/w780$it"},
        overview = overview,
        popularity = popularity,
        voteAverage = voteAverage,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle
    )
}