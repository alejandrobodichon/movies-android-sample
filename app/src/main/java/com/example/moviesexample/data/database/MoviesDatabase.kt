package com.example.moviesexample.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities =[MovieLocal::class], version = 1, exportSchema = false)
abstract class MoviesDatabase: RoomDatabase() {
    abstract val moviesDao: MoviesDao
}