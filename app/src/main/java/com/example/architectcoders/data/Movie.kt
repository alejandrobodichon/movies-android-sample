package com.example.architectcoders.data

data class Movie(
    val id: Int,
    val title: String,
    val poster: String
)

val movies = (1..100).map{
    Movie(
        id = it,
        title = "Movie $it",
        poster = "https://loremflickr.com/320/240?lock=$it"
    )
}