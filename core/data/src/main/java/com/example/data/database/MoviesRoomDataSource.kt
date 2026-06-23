package com.example.data.database

import com.example.moviesexample.domain.datasource.MoviesLocalDataSource
import com.example.moviesexample.domain.entity.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoviesRoomDataSource @Inject constructor(private val moviesDao: MoviesDao) :
    MoviesLocalDataSource {
    override val movies: Flow<List<Movie>> =
        moviesDao.fetchPopularMovies().map { it.toDomainMovies()}

    override fun findMovieById(id: Int) =
        moviesDao.findMovieById(id).map{ it?.toDomainMovie()}
    override suspend fun saveMovies(movies: List<Movie>) =
        moviesDao.saveMovies(movies.map { it.toDbMovie()})

    fun Movie.toDbMovie() = DbMovie(
        id = id,
        poster = poster,
        title = title,
        releaseDate = releaseDate,
        overview = overview,
        backdrop = backdrop,
        originalTitle = originalTitle,
        originalLanguage = originalLanguage,
        popularity = popularity,
        voteAverage = voteAverage,
        favorite = favorite
    )
}

private fun List<DbMovie>.toDomainMovies(): List<Movie> = map{ it.toDomainMovie() }
