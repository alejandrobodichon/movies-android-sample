package com.example.moviesexample.data

import com.example.moviesexample.data.database.MovieLocal
import com.example.moviesexample.data.datasource.MoviesLocalDataSource
import com.example.moviesexample.data.datasource.MoviesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class MoviesRepository(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val regionRepository: RegionRepository,
    private val localDataSource: MoviesLocalDataSource
) {
    val movies: Flow<List<Movie>> = localDataSource.movies.transform { localMovies ->
        if (localMovies.isEmpty()) {
            val movies = remoteDataSource.fetchPopularMovies(regionRepository.findLastRegion())
            localDataSource.saveMovies(movies.map { it.toLocalModel() })
        } else {
            emit(localMovies.map { it.toDomainMovie() })
        }
    }

    fun findMovieById(id: Int): Flow<Movie> =
        localDataSource.findMovieById(id).transform { localMovie ->
            if (localMovie == null) {
                val movie = remoteDataSource.findMovieById(id)
                localDataSource.saveMovies(listOf(movie.toLocalModel()))
            } else {
                emit(localMovie.toDomainMovie())
            }
        }


    suspend fun updateMovie(movie: Movie?) {
        movie?.let { movie ->
            localDataSource.saveMovies(listOf(movie.toLocalModel()))
        }
    }
}

