package com.example.data.remote

import com.example.moviesexample.domain.datasource.MoviesRemoteDataSource
import com.example.moviesexample.domain.entity.Movie
import javax.inject.Inject

class MoviesServerDataSource @Inject constructor(
    private val moviesService: MoviesService
) : MoviesRemoteDataSource {

    override suspend fun fetchPopularMovies(findLastRegion: String): List<Movie> {
        val movies = moviesService
            .getPopularMovies(findLastRegion)
            .results
            .map { it.toDomainModel() }
        return movies
    }

    override suspend fun findMovieById(id: Int): Movie {
        val movie = moviesService
            .fetchMovieById(id)
            .toDomainModel()
        return movie
    }

    fun RemoteMovie.toDomainModel() = Movie(
        poster = "https://image.tmdb.org/t/p/w500$posterPath",
        title = title,
        id = id,
        releaseDate = releaseDate,
        backdrop = backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" },
        overview = overview,
        popularity = popularity,
        voteAverage = voteAverage,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle
    )
}
