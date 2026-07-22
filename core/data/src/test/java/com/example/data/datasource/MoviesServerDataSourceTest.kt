package com.example.data.datasource

import com.example.data.mockwebserver.MockWebServerRule
import com.example.data.mockwebserver.fromJson
import com.example.data.remote.MoviesServerDataSource
import com.example.data.remote.MoviesService
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import jakarta.inject.Inject
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class MoviesServerDataSourceTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mockWebServerRule = MockWebServerRule()

    @Inject
    lateinit var moviesService: MoviesService

    private lateinit var dataSource: MoviesServerDataSource

    @Before
    fun setUp() {
        hiltRule.inject()
        MockitoAnnotations.openMocks(this)
        dataSource = MoviesServerDataSource(moviesService)
    }

    private fun enqueue(fixture: String) {
        mockWebServerRule.server.enqueue(
            MockResponse().setResponseCode(200).fromJson(fixture)
        )
    }

    @Test
    fun `getPopularMovies parses poster path from response`() = runTest {
        // Given
        enqueue("popular_movies_poster.json")

        // When
        val result = moviesService.getPopularMovies("US")

        // Then
        assertEquals("/poster.jpg", result.results[0].posterPath)
    }

    @Test
    fun `fetchPopularMovies maps poster path to full URL`() = runTest {
        enqueue("popular_movies_poster.json")

        val result = dataSource.fetchPopularMovies("US")

        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", result[0].poster)
    }

    @Test
    fun `fetchPopularMovies maps backdrop path to full URL when present`() = runTest {
        enqueue("popular_movies_backdrop.json")

        val result = dataSource.fetchPopularMovies("US")

        assertEquals("https://image.tmdb.org/t/p/w780/backdrop.jpg", result[0].backdrop)
    }

    @Test
    fun `fetchPopularMovies maps null backdrop to null`() = runTest {
        enqueue("popular_movies_null_backdrop.json")

        val result = dataSource.fetchPopularMovies("US")

        assertNull(result[0].backdrop)
    }

    @Test
    fun `fetchPopularMovies returns all movies from results`() = runTest {
        enqueue("popular_movies_three.json")

        val result = dataSource.fetchPopularMovies("US")

        assertEquals(3, result.size)
    }

    @Test
    fun `fetchPopularMovies maps all domain fields correctly`() = runTest {
        enqueue("popular_movies_all_fields.json")

        val result = dataSource.fetchPopularMovies("US")[0]

        assertEquals(42, result.id)
        assertEquals("Title 42", result.title)
        assertEquals("2024-01-01", result.releaseDate)
        assertEquals("Overview 42", result.overview)
        assertEquals(7.5, result.popularity, 0.0)
        assertEquals(8.0, result.voteAverage, 0.0)
        assertEquals("en", result.originalLanguage)
        assertEquals("Original Title 42", result.originalTitle)
    }

    @Test
    fun `findMovieById maps poster path to full URL`() = runTest {
        enqueue("movie_detail_poster.json")

        val result = dataSource.findMovieById(1)

        assertEquals("https://image.tmdb.org/t/p/w500/poster.jpg", result.poster)
    }

    @Test
    fun `findMovieById maps backdrop path to full URL when present`() = runTest {
        enqueue("movie_detail_backdrop.json")

        val result = dataSource.findMovieById(1)

        assertEquals("https://image.tmdb.org/t/p/w780/backdrop.jpg", result.backdrop)
    }

    @Test
    fun `findMovieById maps null backdrop to null`() = runTest {
        enqueue("movie_detail_null_backdrop.json")

        val result = dataSource.findMovieById(1)

        assertNull(result.backdrop)
    }

    @Test
    fun `findMovieById maps all domain fields correctly`() = runTest {
        enqueue("movie_detail_all_fields.json")

        val result = dataSource.findMovieById(7)

        assertEquals(7, result.id)
        assertEquals("Title 7", result.title)
        assertEquals("Overview 7", result.overview)
        assertEquals(7.5, result.popularity, 0.0)
        assertEquals(8.0, result.voteAverage, 0.0)
        assertEquals("en", result.originalLanguage)
        assertEquals("Original Title 7", result.originalTitle)
    }
}
