package com.example.data.repository

import com.example.moviesexample.domain.entity.Movie
import com.example.moviesexample.domain.datasource.MoviesLocalDataSource
import com.example.moviesexample.domain.datasource.MoviesRemoteDataSource
import com.example.moviesexample.domain.repository.IMoviesRepository
import com.example.moviesexample.domain.repository.IRegionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val regionRepository: IRegionRepository,
    private val localDataSource: MoviesLocalDataSource
) : IMoviesRepository {

    override val movies: Flow<List<Movie>> = localDataSource.movies.onEach { localMovies ->
        if (localMovies.isEmpty()) {
            val remoteMoves = remoteDataSource.fetchPopularMovies(regionRepository.findLastRegion())
            localDataSource.saveMovies(remoteMoves)
        }
    }.map { localMovies -> localMovies }

    override fun findMovieById(id: Int): Flow<Movie> =
        localDataSource.findMovieById(id).onEach { localMovie ->
            if (localMovie == null) {
                val movie = remoteDataSource.findMovieById(id)
                localDataSource.saveMovies(listOf(movie))
            }
        }.filterNotNull()
            .map { localMovie -> localMovie }

    override suspend fun toggleFavorite(movie: Movie) {
        localDataSource.saveMovies(listOf(
            movie.copy(favorite = !movie.favorite))
        )
    }

}