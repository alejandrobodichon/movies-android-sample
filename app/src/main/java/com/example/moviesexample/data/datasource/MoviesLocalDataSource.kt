package com.example.moviesexample.data.datasource

import com.example.moviesexample.framework.database.DbMovie
import kotlinx.coroutines.flow.Flow

interface MoviesLocalDataSource {
    val movies: Flow<List<DbMovie>>
    fun findMovieById(id: Int): Flow<DbMovie?>

    suspend fun saveMovies(movies: List<DbMovie>)
}