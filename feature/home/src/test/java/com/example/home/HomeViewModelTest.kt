package com.example.home

import app.cash.turbine.test
import com.example.moviesexample.domain.entity.AppError
import com.example.moviesexample.domain.entity.Movie
import com.example.moviesexample.domain.entity.Result
import com.example.moviesexample.domain.repository.IMoviesRepository
import com.example.moviesexample.domain.usecase.FetchMoviesUseCase
import com.example.moviesexample.domain.usecase.RefreshMoviesUseCase
import com.example.testing.sampleMovies
import com.example.testing.testrules.CoroutinesTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException

class HomeViewModelTest {

    @get:Rule
    val coroutineRule = CoroutinesTestRule(testDispatcher = StandardTestDispatcher())

    private lateinit var repository: IMoviesRepository

    private fun buildVm(localMovies: List<Movie> = emptyList()): HomeViewModel {
        repository = mock {
            on { movies } doReturn flowOf(localMovies)
        }
        return HomeViewModel(
            fetchMoviesUseCase = FetchMoviesUseCase(repository),
            refreshMoviesUseCase = RefreshMoviesUseCase(repository)
        )
    }

    @Test
    fun `state is Loading before onUiReady`() = runTest {
        val vm = buildVm()

        vm.state.test {
            assertEquals(Result.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state emits Success with movies after onUiReady`() = runTest {
        val movies = sampleMovies(1, 2, 3)
        val vm = buildVm(localMovies = movies)

        vm.state.test {
            assertEquals(Result.Loading, awaitItem())
            vm.onUiReady()
            assertEquals(Result.Success(movies), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state emits Error when repository throws`() = runTest {
        repository = mock {
            on { movies } doReturn flow { throw IOException() }
        }
        val vm = HomeViewModel(
            fetchMoviesUseCase = FetchMoviesUseCase(repository),
            refreshMoviesUseCase = RefreshMoviesUseCase(repository)
        )

        vm.state.test {
            assertEquals(Result.Loading, awaitItem())
            vm.onUiReady()
            assertEquals(Result.Error(AppError.Network.NoInternet), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `refresh calls repository refresh`() = runTest {
        val vm = buildVm()
        whenever(repository.refresh()).doReturn(Result.Success(Unit))

        vm.refresh()
        advanceUntilIdle()

        verify(repository).refresh()
    }

    @Test
    fun `isRefreshing is true during refresh and false after`() = runTest {
        val vm = buildVm()
        whenever(repository.refresh()).doReturn(Result.Success(Unit))

        vm.isRefreshing.test {
            assertFalse(awaitItem())
            vm.refresh()
            assertTrue(awaitItem())
            assertFalse(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh emits error event when repository returns error`() = runTest {
        val vm = buildVm()
        whenever(repository.refresh()).doReturn(Result.Error(AppError.Network.Timeout))

        vm.errorEvent.test {
            vm.refresh()
            assertEquals(AppError.Network.Timeout, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
