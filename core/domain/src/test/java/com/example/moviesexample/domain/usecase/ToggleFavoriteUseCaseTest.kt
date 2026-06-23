package com.example.moviesexample.domain.usecase

import com.example.moviesexample.domain.repository.IMoviesRepository
import com.example.testing.sampleMovie
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify


class ToggleFavoriteUseCaseTest {
    @Test
    fun `Invoke calls repository`(): Unit = runTest{
        //Before
        val movie = sampleMovie(1)
        val repository = mock(IMoviesRepository::class.java)
        val useCase = ToggleFavoriteUseCase(repository)

        //When
        useCase(movie)

        //Then
        verify(repository).toggleFavorite(movie)
    }
}