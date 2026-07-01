package com.example.ui.model

import com.example.moviesexample.domain.entity.Movie

fun Movie.toUiModel() =
        MovieUiModel(
            id = id,
            poster = poster,
            title = title,
            releaseDate = releaseDate,
            overview = overview,
            backdrop = backdrop,
            originalTitle = originalTitle,
            originalLanguage = originalLanguage,
            popularity = popularity,
            voteAverage = voteAverage
        )