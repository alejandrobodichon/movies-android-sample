package com.example.testing

import com.example.moviesexample.domain.entity.Movie

fun sampleMovie(id: Int) = Movie(
    id = id,
    poster = "nominavi",
    title = "scelerisque",
    releaseDate = "egestas",
    overview = "vituperatoribus",
    backdrop = "dolorum",
    originalTitle = "propriae",
    originalLanguage = "dicat",
    popularity = 4.5,
    voteAverage = 6.7,
    favorite = false
)

fun sampleMovies(vararg ids: Int ) = ids.map { sampleMovie(it) }