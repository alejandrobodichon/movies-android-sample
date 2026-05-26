package com.example.moviesexample.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
@Query("SELECT * FROM MovieLocal")
fun fetchPopularMovies(): Flow<List<MovieLocal>>

@Query("SELECT * FROM MovieLocal WHERE id = :id")
fun findMovieById(id: Int): Flow<MovieLocal?>

@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun saveMovies(movies: List<MovieLocal>)

}