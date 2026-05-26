package com.example.moviesexample.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MoviesDao {
@Query("SELECT * FROM MovieLocal")
suspend fun fetchPopularMovies(): List<MovieLocal>

@Query("SELECT * FROM MovieLocal WHERE id = :id")
suspend fun findMovieById(id: Int): MovieLocal?

@Query("SELECT COUNT(*) FROM MovieLocal")
suspend fun countMovies(): Int

@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun saveMovies(movies: List<MovieLocal>)

}