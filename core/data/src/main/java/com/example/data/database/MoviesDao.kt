package com.example.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Query(
        "SELECT movies.*, \n" +
            "               CASE WHEN favorites.movieId IS NOT NULL THEN 1 ELSE 0 END AS favorite\n" +
            "        FROM movies\n" +
            "        LEFT JOIN favorites ON movies.id = favorites.movieId"
    )
    fun fetchPopularMovies(): Flow<List<DbMovieWithFavorite>>

    @Query(
        "SELECT movies.*, \n" +
            "               CASE WHEN favorites.movieId IS NOT NULL THEN 1 ELSE 0 END AS favorite\n" +
            "        FROM movies\n" +
            "        LEFT JOIN favorites ON movies.id = favorites.movieId\n" +
            "        WHERE movies.id = :id"
    )
    fun findMovieById(id: Int): Flow<DbMovieWithFavorite?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovies(movies: List<DbMovie>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favorite: DbFavorite)

    @Delete
    suspend fun removeFavorite(favorite: DbFavorite)

}
