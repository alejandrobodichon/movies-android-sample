package com.example.data.database

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DbMovieTest {

    private fun sampleDbMovie() = DbMovie(
        id = 1,
        poster = "poster_url",
        title = "Movie Title",
        releaseDate = "2024-01-01",
        overview = "Overview text",
        backdrop = "backdrop_url",
        originalTitle = "Original Title",
        originalLanguage = "en",
        popularity = 8.5,
        voteAverage = 7.2,
        favorite = false
    )

    @Test
    fun `toDomainMovie maps all fields correctly`() {
        val dbMovie = sampleDbMovie()

        val movie = dbMovie.toDomainMovie()

        assertEquals(dbMovie.id, movie.id)
        assertEquals(dbMovie.poster, movie.poster)
        assertEquals(dbMovie.title, movie.title)
        assertEquals(dbMovie.releaseDate, movie.releaseDate)
        assertEquals(dbMovie.overview, movie.overview)
        assertEquals(dbMovie.backdrop, movie.backdrop)
        assertEquals(dbMovie.originalTitle, movie.originalTitle)
        assertEquals(dbMovie.originalLanguage, movie.originalLanguage)
        assertEquals(dbMovie.popularity, movie.popularity, 0.0)
        assertEquals(dbMovie.voteAverage, movie.voteAverage, 0.0)
        assertEquals(dbMovie.favorite, movie.favorite)
    }

    @Test
    fun `toDomainMovie maps null backdrop correctly`() {
        val dbMovie = sampleDbMovie().copy(backdrop = null)

        val movie = dbMovie.toDomainMovie()

        assertNull(movie.backdrop)
    }

    @Test
    fun `toDomainMovie maps favorite true correctly`() {
        val dbMovie = sampleDbMovie().copy(favorite = true)

        val movie = dbMovie.toDomainMovie()

        assertEquals(true, movie.favorite)
    }
}
