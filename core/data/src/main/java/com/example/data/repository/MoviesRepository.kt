package com.example.data.repository

import com.example.data.extension.toAppError
import com.example.moviesexample.domain.entity.Movie
import com.example.moviesexample.domain.datasource.MoviesLocalDataSource
import com.example.moviesexample.domain.datasource.MoviesRemoteDataSource
import com.example.moviesexample.domain.datasource.RegionDataSource
import com.example.moviesexample.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import com.example.moviesexample.domain.entity.Result

class MoviesRepository @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val regionDataSource: RegionDataSource,
    private val localDataSource: MoviesLocalDataSource
) : IMoviesRepository {

    override val movies: Flow<List<Movie>> = localDataSource.movies.onEach { localMovies ->
        if (localMovies.isEmpty()) {
            requestFromRemote()
        }
    }

    private suspend fun requestFromRemote() {
        val remoteMovies = remoteDataSource.fetchPopularMovies(
            regionDataSource.findLastRegion()
        )
        localDataSource.saveMovies(remoteMovies)
    }

    override suspend fun refresh(): Result<Unit> = try {
        requestFromRemote()
        Result.Success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.Error(e.toAppError())
    }

    override fun findMovieById(id: Int): Flow<Movie> =
        localDataSource.findMovieById(id).onEach { localMovie ->
            if (localMovie == null) {
                val movie = remoteDataSource.findMovieById(id)
                localDataSource.saveMovies(listOf(movie))
            }
        }.filterNotNull()

    override suspend fun toggleFavorite(movie: Movie) {
        localDataSource.updateFavorite(movie.id, !movie.favorite)
    }

}