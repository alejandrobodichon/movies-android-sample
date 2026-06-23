package com.example.moviesexample.domain.usecase

import com.example.moviesexample.domain.entity.Movie
import com.example.moviesexample.domain.repository.IMoviesRepository
import com.example.testing.sampleMovie
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


class FindMovieByIdUseCaseTest {

    @Test
    fun `Invoke calls repository`() {
        //Before
        val movie = flowOf(sampleMovie(1))
        val repository = mock(IMoviesRepository::class.java)
        val useCase = FindMovieByIdUseCase(repository)
        whenever(repository.findMovieById(1)).doReturn(movie)

        //When
        val result = useCase(1)

        //Then
        verify(repository).findMovieById(1)
        assertEquals(movie, result)
    }
}