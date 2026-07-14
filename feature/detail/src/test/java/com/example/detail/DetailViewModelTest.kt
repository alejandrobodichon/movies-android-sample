package com.example.detail

import app.cash.turbine.test
import com.example.moviesexample.domain.entity.AppError
import com.example.moviesexample.domain.entity.Movie
import com.example.moviesexample.domain.entity.Result
import com.example.moviesexample.domain.repository.IMoviesRepository
import com.example.moviesexample.domain.usecase.FindMovieByIdUseCase
import com.example.moviesexample.domain.usecase.ToggleFavoriteUseCase
import com.example.testing.sampleMovie
import com.example.testing.testrules.CoroutinesTestRule
import com.example.ui.model.toUiModel
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
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule
    val coroutineRule = CoroutinesTestRule(testDispatcher = StandardTestDispatcher())

    private lateinit var repository: IMoviesRepository

    private fun buildVm(id: Int = 1, movie: Movie = sampleMovie(id)): DetailViewModel {
        repository = mock {
            on { findMovieById(id) } doReturn flowOf(movie)
        }
        return DetailViewModel(
            movieId = id,
            findMovieByIdUseCase = FindMovieByIdUseCase(repository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(repository)
        )
    }

    @Test
    fun `movieState emits Loading then Success`() = runTest {
        val movie = sampleMovie(1)
        val vm = buildVm(id = 1, movie = movie)

        vm.movieState.test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(movie.toUiModel()), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `favoriteState reflects movie favorite field`() = runTest {
        val movie = sampleMovie(1).copy(favorite = true)
        val vm = buildVm(id = 1, movie = movie)

        vm.favoriteState.test {
            assertFalse(awaitItem()) // initialValue = false
            assertTrue(awaitItem())  // movie.favorite = true
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `movieState emits Error when repository throws`() = runTest {
        repository = mock {
            on { findMovieById(1) } doReturn flow { throw IOException() }
        }
        val vm = DetailViewModel(
            movieId = 1,
            findMovieByIdUseCase = FindMovieByIdUseCase(repository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(repository)
        )
        vm.movieState.test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Error(AppError.Network.NoInternet), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onFavoriteClick calls toggleFavorite on repository`() = runTest {
        val movie = sampleMovie(1)
        val vm = buildVm(id = 1, movie = movie)

        vm.movieState.test {
            skipItems(2) // Loading + Success
            vm.onFavoriteClick()
            cancelAndIgnoreRemainingEvents()
        }
        advanceUntilIdle()

        verify(repository).toggleFavorite(movie.id, movie.favorite)
    }
}
