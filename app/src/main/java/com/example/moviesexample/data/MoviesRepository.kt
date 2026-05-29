package com.example.moviesexample.data

import com.example.moviesexample.framework.database.DbMovie
import com.example.moviesexample.data.datasource.MoviesLocalDataSource
import com.example.moviesexample.data.datasource.MoviesRemoteDataSource
import com.example.moviesexample.domain.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.collections.map

class MoviesRepository @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val regionRepository: RegionRepository,
    private val localDataSource: MoviesLocalDataSource
) {

    val movies: Flow<List<Movie>> = localDataSource.movies.onEach { localMovies ->
        if (localMovies.isEmpty()) {
            val remoteMoves = remoteDataSource.fetchPopularMovies(regionRepository.findLastRegion())
            localDataSource.saveMovies(remoteMoves.map { it.toDbMovie() })
        }
    }.map { localMovies -> localMovies.map { it.toDomainMovie() } }

    fun findMovieById(id: Int): Flow<Movie> =
        localDataSource.findMovieById(id).onEach { localMovie ->
            if (localMovie == null) {
                val movie = remoteDataSource.findMovieById(id)
                localDataSource.saveMovies(listOf(movie.toDbMovie()))
            }
        }.filterNotNull()
            .map { localMovie -> localMovie.toDomainMovie() }

    suspend fun toggleFavorite(movie: Movie) {
        localDataSource.saveMovies(listOf(
            movie.copy(favorite = !movie.favorite).toDbMovie())
        )
    }

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

