package com.example.moviesexample.data.datasource

import com.example.moviesexample.data.database.MovieLocal
import com.example.moviesexample.data.database.MoviesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesLocalDataSource @Inject constructor(private val moviesDao: MoviesDao) {
    val movies: Flow<List<MovieLocal>> = moviesDao.fetchPopularMovies()
    fun findMovieById(id: Int) = moviesDao.findMovieById(id)
    suspend fun saveMovies(movies: List<MovieLocal>) = moviesDao.saveMovies(movies)
}
