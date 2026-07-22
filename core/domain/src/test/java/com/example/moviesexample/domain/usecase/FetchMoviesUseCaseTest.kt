package com.example.moviesexample.domain.usecase

import com.example.testing.sampleMovies
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class FetchMoviesUseCaseTest {

    @Test
    fun `Invoke calls repository`() {
        val moviesFlow = flowOf(sampleMovies(1, 2, 3))
        val useCase = FetchMoviesUseCase(
            mock {
                on { movies } doReturn moviesFlow
            }
        )
        assertEquals(moviesFlow, useCase())
    }

}
