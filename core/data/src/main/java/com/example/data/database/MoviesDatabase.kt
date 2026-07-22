package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DbMovie::class, DbFavorite::class], version = 2, exportSchema = false)
abstract class MoviesDatabase: RoomDatabase() {
    abstract val moviesDao: MoviesDao
}
