package com.example.data.repository

import com.example.moviesexample.domain.datasource.MoviesLocalDataSource
import com.example.moviesexample.domain.datasource.MoviesRemoteDataSource
import com.example.moviesexample.domain.datasource.RegionDataSource
import com.example.testing.sampleMovies
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import com.example.moviesexample.domain.entity.Movie
import com.example.testing.sampleMovie
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import org.mockito.MockitoAnnotations


class MoviesRepositoryTest {

    @Mock
    private lateinit var remoteDataSource: MoviesRemoteDataSource

    @Mock
    private lateinit var regionDataSource: RegionDataSource

    @Mock
    private lateinit var localDataSource: MoviesLocalDataSource

    private lateinit var repository: MoviesRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    private suspend fun mockFetchDataSources(localMovies: List<Movie> = emptyList(), remoteMovies: List<Movie> = emptyList()) {
        whenever(localDataSource.movies).doReturn(flowOf(localMovies))
        whenever(remoteDataSource.fetchPopularMovies("US")).doReturn(remoteMovies)
        repository = MoviesRepository(remoteDataSource, regionDataSource, localDataSource)
    }

    private suspend fun mockFindByIdDataSources(localMovie: Movie? = null, remoteMovie: Movie? = null) {
        whenever(localDataSource.findMovieById(any())).doReturn(flowOf(localMovie))
        if(remoteMovie!=null){
            whenever(remoteDataSource.findMovieById(any())).doReturn(remoteMovie)
            }
        repository = MoviesRepository(remoteDataSource, regionDataSource, localDataSource)
    }

    @Test
    fun `Popular movies are taken from local data source if available`() = runTest {
        //Before
        val movies = sampleMovies(1,2)
        mockFetchDataSources(localMovies = movies)

        //When
        val result = repository.movies

        //Then
        verify(remoteDataSource, times(0)).fetchPopularMovies(any())
        assertEquals(movies, result.first())

    }

    @Test
    fun `Popular movies are taken from remote data source `() = runTest {
        //Before
        val movies = sampleMovies(1,2)
        whenever(regionDataSource.findLastRegion()).doReturn("US")
        mockFetchDataSources(remoteMovies = movies)

        //When
        repository.movies.first()

        //Then
        verify(remoteDataSource, times(1)).fetchPopularMovies(any())
        verify(localDataSource).saveMovies(movies)
    }

    @Test
    fun `Find movie by Id taken from local data source `() = runTest {
        //Before
        val movie = sampleMovie(1)
        mockFindByIdDataSources(localMovie = movie)

        //When
        val result = repository.findMovieById(1).first()

        //Then
        verify(remoteDataSource, times(0)).findMovieById(any())
        assertEquals(result, movie)
    }

    @Test
    fun `Find movie by Id taken from remote data source `() = runTest {
        //Before
        val movie = sampleMovie(1)
        mockFindByIdDataSources(remoteMovie = movie)

        //When
        repository.findMovieById(1).collect()

        //Then
        verify(remoteDataSource, times(1)).findMovieById(any())
        verify(localDataSource, times(1)).saveMovies(listOf(movie))
        verify(localDataSource).saveMovies(listOf(movie))
    }

    @Test
    fun `Toggle favorite updates local data source`() = runTest {
        //Before
        val movie = sampleMovie(1)
        mockFindByIdDataSources(localMovie = movie)
        //When
        repository.toggleFavorite(movie)
        //Then
        verify(localDataSource).saveMovies(listOf(movie.copy(favorite = !movie.favorite)))
    }

}