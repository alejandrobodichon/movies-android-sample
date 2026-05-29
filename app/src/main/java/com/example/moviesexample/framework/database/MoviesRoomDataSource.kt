package com.example.moviesexample.framework.database

import com.example.moviesexample.data.datasource.MoviesLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRoomDataSource @Inject constructor(private val moviesDao: MoviesDao) :
    MoviesLocalDataSource {
    override val movies: Flow<List<DbMovie>> = moviesDao.fetchPopularMovies()
    override fun findMovieById(id: Int) = moviesDao.findMovieById(id)
    override suspend fun saveMovies(movies: List<DbMovie>) = moviesDao.saveMovies(movies)
}