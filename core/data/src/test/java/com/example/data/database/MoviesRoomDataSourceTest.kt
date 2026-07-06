package com.example.data.database

import com.example.testing.sampleMovie
import com.example.testing.sampleMovies
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class MoviesRoomDataSourceTest {

    @Mock
    private lateinit var moviesDao: MoviesDao

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `movies emits mapped domain movies from DAO`() = runTest {
        val dbMovies = listOf(
            sampleDbMovie(1),
            sampleDbMovie(2)
        )
        whenever(moviesDao.fetchPopularMovies()).thenReturn(flowOf(dbMovies))
        val dataSource = MoviesRoomDataSource(moviesDao)

        val result = dataSource.movies.first()

        assertEquals(2, result.size)
        assertEquals(1, result[0].id)
        assertEquals(2, result[1].id)
    }

    @Test
    fun `movies emits empty list when DAO returns no movies`() = runTest {
        whenever(moviesDao.fetchPopularMovies()).thenReturn(flowOf(emptyList()))
        val dataSource = MoviesRoomDataSource(moviesDao)

        val result = dataSource.movies.first()

        assertEquals(emptyList<Any>(), result)
    }

    @Test
    fun `findMovieById returns mapped domain movie`() = runTest {
        val dbMovie = sampleDbMovie(5)
        whenever(moviesDao.fetchPopularMovies()).thenReturn(flowOf(emptyList()))
        whenever(moviesDao.findMovieById(5)).thenReturn(flowOf(dbMovie))
        val dataSource = MoviesRoomDataSource(moviesDao)

        val result = dataSource.findMovieById(5).first()

        assertEquals(5, result?.id)
        assertEquals(dbMovie.movie.title, result?.title)
    }

    @Test
    fun `findMovieById returns null when movie not found`() = runTest {
        whenever(moviesDao.fetchPopularMovies()).thenReturn(flowOf(emptyList()))
        whenever(moviesDao.findMovieById(99)).thenReturn(flowOf(null))
        val dataSource = MoviesRoomDataSource(moviesDao)

        val result = dataSource.findMovieById(99).first()

        assertNull(result)
    }

    @Test
    fun `saveMovies converts domain movies to DbMovies and delegates to DAO`() = runTest {
        val movies = sampleMovies(1, 2)
        whenever(moviesDao.fetchPopularMovies()).thenReturn(flowOf(emptyList()))
        val dataSource = MoviesRoomDataSource(moviesDao)
        val captor = argumentCaptor<List<DbMovie>>()

        dataSource.saveMovies(movies)

        verify(moviesDao).saveMovies(captor.capture())
        val saved = captor.firstValue
        assertEquals(2, saved.size)
        assertEquals(movies[0].id, saved[0].id)
        assertEquals(movies[0].title, saved[0].title)
        assertEquals(movies[1].id, saved[1].id)
    }

    @Test
    fun `saveMovies preserves null backdrop when converting to DbMovie`() = runTest {
        val movie = sampleMovie(1).copy(backdrop = null)
        whenever(moviesDao.fetchPopularMovies()).thenReturn(flowOf(emptyList()))
        val dataSource = MoviesRoomDataSource(moviesDao)
        val captor = argumentCaptor<List<DbMovie>>()

        dataSource.saveMovies(listOf(movie))

        verify(moviesDao).saveMovies(captor.capture())
        assertNull(captor.firstValue[0].backdrop)
    }

    private fun sampleDbMovie(id: Int) = DbMovieWithFavorite(
        movie = DbMovie(
            id = id,
            poster = "poster_$id",
            title = "Title $id",
            releaseDate = "2024-01-01",
            overview = "Overview $id",
            backdrop = "backdrop_$id",
            originalTitle = "Original $id",
            originalLanguage = "en",
            popularity = 7.5,
            voteAverage = 8.0,
        ),
        favorite = false
    )
}
