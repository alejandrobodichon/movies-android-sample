package com.example.architectcoders.data

import com.example.architectcoders.data.database.MovieLocal


data class Movie(
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
){
    fun toLocalModel() = MovieLocal(
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