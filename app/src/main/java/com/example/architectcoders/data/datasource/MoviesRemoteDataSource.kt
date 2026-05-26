package com.example.architectcoders.data.datasource

import com.example.architectcoders.data.Movie
import com.example.architectcoders.data.remote.MoviesClient
import com.example.architectcoders.data.remote.MoviesService
import com.example.architectcoders.data.remote.RemoteMovie

class MoviesRemoteDataSource(
    private val moviesService: MoviesService = MoviesClient.instance) {

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