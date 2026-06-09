package com.example.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class MovieUiModel(
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