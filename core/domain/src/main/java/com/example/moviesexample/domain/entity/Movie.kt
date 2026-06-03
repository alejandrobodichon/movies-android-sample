package com.example.moviesexample.domain.entity

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

}