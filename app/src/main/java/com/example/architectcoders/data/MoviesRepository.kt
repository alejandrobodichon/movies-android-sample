package com.example.architectcoders.data

import com.example.architectcoders.data.datasource.MoviesLocalDataSource
import com.example.architectcoders.data.datasource.MoviesRemoteDataSource

class MoviesRepository(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val regionRepository: RegionRepository,
    private val localDataSource: MoviesLocalDataSource
){
    suspend fun fetchPopularMovies(): List<Movie> {
        if(localDataSource.isEmpty()){
            val remoteMovies = remoteDataSource.fetchPopularMovies(regionRepository.findLastRegion())
            localDataSource.saveMovies(remoteMovies.map { it.toLocalModel() })
        }

        return localDataSource.fetchPopularMovies().map { it.toDomainMovie() }
    }

    suspend fun findMovieById(id: Int): Movie{
        if(localDataSource.findMovieById(id) == null){
            val movie = remoteDataSource.findMovieById(id)
            localDataSource.saveMovies(listOf(movie.toLocalModel()))
        }
        return checkNotNull(localDataSource.findMovieById(id)?.toDomainMovie())
    }
}

