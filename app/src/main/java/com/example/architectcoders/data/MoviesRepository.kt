package com.example.architectcoders.data

import kotlinx.coroutines.delay

class MoviesRepository {

    suspend fun fetchPopularMovies(): List<Movie> {
        delay(2000)
        return movies
    }
}