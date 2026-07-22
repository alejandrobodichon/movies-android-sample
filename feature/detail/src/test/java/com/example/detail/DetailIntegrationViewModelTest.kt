package com.example.detail

import app.cash.turbine.test
import com.example.data.buildMoviesRepository
import com.example.moviesexample.domain.entity.Movie
import com.example.testing.sampleMovies
import com.example.testing.testrules.CoroutinesTestRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import com.example.moviesexample.domain.entity.Result
import com.example.moviesexample.domain.usecase.FindMovieByIdUseCase
import com.example.moviesexample.domain.usecase.ToggleFavoriteUseCase
import com.example.ui.model.toUiModel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.collections.emptyList

class DetailIntegrationViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutinesTestRule(testDispatcher = StandardTestDispatcher())

    @Test
    fun `Data is loaded from local data source when available`() = runTest {
        val localData = sampleMovies(1, 2, 3)
        val vm = createViewmodelWithFakes(id = 2, localData = localData)
        vm.movieState.test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(localData[1].toUiModel()), awaitItem())
        }
        vm.favoriteState.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `Data is loaded from server when local data source is empty`() = runTest {
        val remoteMovies = sampleMovies(1, 2, 3)
        val vm = createViewmodelWithFakes(id = 2, remoteData = remoteMovies)
        vm.movieState.test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(remoteMovies[1].toUiModel()), awaitItem())
        }
        vm.favoriteState.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `Toggle favorite updates data source`() = runTest {
        val localMovies = sampleMovies(1, 2, 3)
        val vm = createViewmodelWithFakes(id = 2, localData = localMovies)
        vm.favoriteState.test {
            assertEquals(false, awaitItem())
            vm.onFavoriteClick()
            assertEquals(true, awaitItem())
            vm.onFavoriteClick()
            assertEquals(false, awaitItem())
        }
    }

    private fun createViewmodelWithFakes(id: Int = 1, localData: List<Movie> = emptyList(), remoteData: List<Movie> = emptyList()): DetailViewModel {
        val moviesRepository = buildMoviesRepository(
            localData = localData,
            remoteData = remoteData
        )
        return DetailViewModel(
            movieId = id,
            findMovieByIdUseCase = FindMovieByIdUseCase(moviesRepository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(moviesRepository)
        )
    }
}
