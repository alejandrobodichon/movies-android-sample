package com.example.moviesexample.data.datasource

import com.example.moviesexample.data.database.MovieLocal
import com.example.moviesexample.data.database.MoviesDao

class MoviesLocalDataSource(private val moviesDao: MoviesDao) {
    suspend fun fetchPopularMovies() = moviesDao.fetchPopularMovies()
    suspend fun isEmpty() = moviesDao.countMovies() == 0
    suspend fun findMovieById(id: Int): MovieLocal? = moviesDao.findMovieById(id)
    suspend fun saveMovies(movies: List<MovieLocal>) = moviesDao.saveMovies(movies)
}
