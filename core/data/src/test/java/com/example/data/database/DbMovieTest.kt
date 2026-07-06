package com.example.data.database

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DbMovieTest {

    private fun sampleDbMovieWithFavorite(favorite: Boolean = false) = DbMovieWithFavorite(
        movie = DbMovie(
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
        ),
        favorite = favorite
    )

    @Test
    fun `toDomainMovie maps all fields correctly`() {
        val dbMovie = sampleDbMovieWithFavorite()

        val movie = dbMovie.toDomainMovie()

        assertEquals(dbMovie.movie.id, movie.id)
        assertEquals(dbMovie.movie.poster, movie.poster)
        assertEquals(dbMovie.movie.title, movie.title)
        assertEquals(dbMovie.movie.releaseDate, movie.releaseDate)
        assertEquals(dbMovie.movie.overview, movie.overview)
        assertEquals(dbMovie.movie.backdrop, movie.backdrop)
        assertEquals(dbMovie.movie.originalTitle, movie.originalTitle)
        assertEquals(dbMovie.movie.originalLanguage, movie.originalLanguage)
        assertEquals(dbMovie.movie.popularity, movie.popularity, 0.0)
        assertEquals(dbMovie.movie.voteAverage, movie.voteAverage, 0.0)
        assertEquals(dbMovie.favorite, movie.favorite)
    }

    @Test
    fun `toDomainMovie maps null backdrop correctly`() {
        val dbMovie = sampleDbMovieWithFavorite().let {
            it.copy(movie = it.movie.copy(backdrop = null))
        }

        val movie = dbMovie.toDomainMovie()

        assertNull(movie.backdrop)
    }

    @Test
    fun `toDomainMovie maps favorite true correctly`() {
        val dbMovie = sampleDbMovieWithFavorite(favorite = true)

        val movie = dbMovie.toDomainMovie()

        assertEquals(true, movie.favorite)
    }
}
