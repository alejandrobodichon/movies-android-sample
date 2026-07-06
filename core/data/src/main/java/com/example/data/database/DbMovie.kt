package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviesexample.domain.entity.Movie


@Entity(tableName = "movies")
data class DbMovie(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val poster: String,
    val title: String,
    val releaseDate: String,
    val overview: String,
    val backdrop: String?,
    val originalTitle: String,
    val originalLanguage: String,
    val popularity: Double,
    val voteAverage: Double,
)