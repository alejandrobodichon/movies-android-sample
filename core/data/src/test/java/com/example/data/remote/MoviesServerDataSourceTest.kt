package com.example.data.remote

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class MoviesServerDataSourceTest {

    @Mock
    private lateinit var moviesService: MoviesService

    private lateinit var dataSource: MoviesServerDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        dataSource = MoviesServerDataSource(moviesService)
    }

    @Test
    fun `fetchPopularMovies maps poster path to full URL`() = runTest {
        val remote = sampleRemoteMovie(1).copy(posterPath = "/poster.jpg")
        whenever(moviesService.getPopularMovies("US")).thenReturn(remoteResult(remote))

        val result = dataSource.fetchPopularMovies("US")

        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", result[0].poster)
    }

    @Test
    fun `fetchPopularMovies maps backdrop path to full URL when present`() = runTest {
        val remote = sampleRemoteMovie(1).copy(backdropPath = "/backdrop.jpg")
        whenever(moviesService.getPopularMovies("US")).thenReturn(remoteResult(remote))

        val result = dataSource.fetchPopularMovies("US")

        assertEquals("https://image.tmdb.org/t/p/w780/backdrop.jpg", result[0].backdrop)
    }

    @Test
    fun `fetchPopularMovies maps null backdrop to null`() = runTest {
        val remote = sampleRemoteMovie(1).copy(backdropPath = null)
        whenever(moviesService.getPopularMovies("US")).thenReturn(remoteResult(remote))

        val result = dataSource.fetchPopularMovies("US")

        assertNull(result[0].backdrop)
    }

    @Test
    fun `fetchPopularMovies returns all movies from results`() = runTest {
        val remotes = listOf(sampleRemoteMovie(1), sampleRemoteMovie(2), sampleRemoteMovie(3))
        whenever(moviesService.getPopularMovies("US")).thenReturn(RemoteResult(1, remotes, 3, 3))

        val result = dataSource.fetchPopularMovies("US")

        assertEquals(3, result.size)
    }

    @Test
    fun `fetchPopularMovies maps all domain fields correctly`() = runTest {
        val remote = sampleRemoteMovie(42)
        whenever(moviesService.getPopularMovies("US")).thenReturn(remoteResult(remote))

        val result = dataSource.fetchPopularMovies("US")[0]

        assertEquals(42, result.id)
        assertEquals(remote.title, result.title)
        assertEquals(remote.releaseDate, result.releaseDate)
        assertEquals(remote.overview, result.overview)
        assertEquals(remote.popularity, result.popularity, 0.0)
        assertEquals(remote.voteAverage, result.voteAverage, 0.0)
        assertEquals(remote.originalLanguage, result.originalLanguage)
        assertEquals(remote.originalTitle, result.originalTitle)
    }

    @Test
    fun `findMovieById maps poster path to full URL`() = runTest {
        val remote = sampleRemoteMovie(1).copy(posterPath = "/poster.jpg")
        whenever(moviesService.fetchMovieById(1)).thenReturn(remote)

        val result = dataSource.findMovieById(1)

        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", result.poster)
    }

    @Test
    fun `findMovieById maps backdrop path to full URL when present`() = runTest {
        val remote = sampleRemoteMovie(1).copy(backdropPath = "/backdrop.jpg")
        whenever(moviesService.fetchMovieById(1)).thenReturn(remote)

        val result = dataSource.findMovieById(1)

        assertEquals("https://image.tmdb.org/t/p/w780/backdrop.jpg", result.backdrop)
    }

    @Test
    fun `findMovieById maps null backdrop to null`() = runTest {
        val remote = sampleRemoteMovie(1).copy(backdropPath = null)
        whenever(moviesService.fetchMovieById(1)).thenReturn(remote)

        val result = dataSource.findMovieById(1)

        assertNull(result.backdrop)
    }

    @Test
    fun `findMovieById maps all domain fields correctly`() = runTest {
        val remote = sampleRemoteMovie(7)
        whenever(moviesService.fetchMovieById(7)).thenReturn(remote)

        val result = dataSource.findMovieById(7)

        assertEquals(7, result.id)
        assertEquals(remote.title, result.title)
        assertEquals(remote.overview, result.overview)
        assertEquals(remote.popularity, result.popularity, 0.0)
        assertEquals(remote.voteAverage, result.voteAverage, 0.0)
        assertEquals(remote.originalLanguage, result.originalLanguage)
        assertEquals(remote.originalTitle, result.originalTitle)
    }

    private fun sampleRemoteMovie(id: Int) = RemoteMovie(
        adult = false,
        backdropPath = "/backdrop_$id.jpg",
        genreIds = listOf(28, 12),
        id = id,
        overview = "Overview $id",
        popularity = 7.5,
        posterPath = "/poster_$id.jpg",
        releaseDate = "2024-01-01",
        title = "Title $id",
        originalTitle = "Original Title $id",
        originalLanguage = "en",
        voteAverage = 8.0
    )

    private fun remoteResult(vararg movies: RemoteMovie) =
        RemoteResult(1, movies.toList(), 1, movies.size)
}
