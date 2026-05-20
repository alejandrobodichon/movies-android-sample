package com.example.architectcoders.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RemoteResult(
    val page: Int,
    val results: List<RemoteMovie>,
    val total_pages: Int,
    val total_results: Int
)
@Serializable
data class RemoteMovie(
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String,
    @SerialName("genre_ids")
    val genreIds: List<Int>? = null,
    val id: Int,
    val overview: String,
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String,
    @SerialName("release_date")
    val releaseDate: String,
    val title: String,

)
