package com.example.data.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class MoviesDaoTest {

    private lateinit var database: MoviesDatabase
    private lateinit var dao: MoviesDao

    private fun dbMovie(id: Int) = DbMovie(
        id = id,
        poster = "poster_$id",
        title = "Movie $id",
        releaseDate = "2024-01-01",
        overview = "Overview $id",
        backdrop = null,
        originalTitle = "Original $id",
        originalLanguage = "en",
        popularity = 1.0,
        voteAverage = 2.0,
    )

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MoviesDatabase::class.java
        ).build()
        dao = database.moviesDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `fetchPopularMovies marks saved favorites as true`() = runTest {
        dao.saveMovies(listOf(dbMovie(1), dbMovie(2)))
        dao.addFavorite(DbFavorite(movieId = 1))

        val movies = dao.fetchPopularMovies().first().sortedBy { it.movie.id }

        assertEquals(2, movies.size)
        assertEquals(true, movies[0].favorite)
        assertEquals(false, movies[1].favorite)
    }

    @Test
    fun `findMovieById returns null when movie not saved`() = runTest {
        val movie = dao.findMovieById(42).first()

        assertNull(movie)
    }

    @Test
    fun `findMovieById returns movie with favorite flag`() = runTest {
        dao.saveMovies(listOf(dbMovie(1)))
        dao.addFavorite(DbFavorite(movieId = 1))

        val movie = dao.findMovieById(1).first()

        assertEquals(1, movie?.movie?.id)
        assertEquals(true, movie?.favorite)
    }

    @Test
    fun `removeFavorite clears favorite flag`() = runTest {
        dao.saveMovies(listOf(dbMovie(1)))
        val favorite = DbFavorite(movieId = 1)
        dao.addFavorite(favorite)

        dao.removeFavorite(favorite)

        val movie = dao.findMovieById(1).first()
        assertEquals(false, movie?.favorite)
    }

    @Test
    fun `saveMovies replaces on conflict`() = runTest {
        dao.saveMovies(listOf(dbMovie(1)))
        val updated = dbMovie(1).copy(title = "Updated title")

        dao.saveMovies(listOf(updated))

        val movie = dao.findMovieById(1).first()
        assertEquals("Updated title", movie?.movie?.title)
    }
}
