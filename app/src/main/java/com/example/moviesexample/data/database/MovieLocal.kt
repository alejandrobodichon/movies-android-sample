package com.example.moviesexample.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviesexample.data.Movie


@Entity
data class MovieLocal(
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
    val favorite: Boolean = false
) {
    fun toDomainMovie(): Movie {
        return Movie(
            id = id,
            poster = poster,
            title = title,
            releaseDate = releaseDate,
            overview = overview,
            backdrop = backdrop,
            originalTitle = originalTitle,
            originalLanguage = originalLanguage,
            popularity = popularity,
            voteAverage = voteAverage,
            favorite = favorite
        )
    }

}