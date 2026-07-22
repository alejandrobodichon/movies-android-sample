package com.example.moviesexample.domain.usecase

import com.example.moviesexample.domain.entity.AppError
import com.example.moviesexample.domain.entity.Result
import com.example.moviesexample.domain.repository.IMoviesRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class RefreshMoviesUseCaseTest {

    @Test
    fun `Invoke calls repository refresh and returns its result`(): Unit = runTest {
        // Before
        val result = Result.Success(Unit)
        val repository = mock<IMoviesRepository> {
            onBlocking { refresh() } doReturn result
        }
        val useCase = RefreshMoviesUseCase(repository)

        // When
        val actual = useCase()

        // Then
        verify(repository).refresh()
        assertEquals(result, actual)
    }

    @Test
    fun `Invoke returns error result when repository refresh fails`(): Unit = runTest {
        // Before
        val error = Result.Error(AppError.Network.ServerError)
        val repository = mock<IMoviesRepository> {
            onBlocking { refresh() } doReturn error
        }
        val useCase = RefreshMoviesUseCase(repository)

        // When
        val actual = useCase()

        // Then
        verify(repository).refresh()
        assertEquals(error, actual)
    }
}
