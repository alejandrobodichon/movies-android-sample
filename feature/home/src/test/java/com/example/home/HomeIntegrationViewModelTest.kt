package com.example.home

import app.cash.turbine.test
import com.example.data.buildMoviesRepository
import com.example.moviesexample.domain.entity.Movie
import com.example.moviesexample.domain.usecase.FetchMoviesUseCase
import com.example.moviesexample.domain.usecase.RefreshMoviesUseCase
import com.example.testing.sampleMovies
import com.example.testing.testrules.CoroutinesTestRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import com.example.moviesexample.domain.entity.Result
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.collections.emptyList

class HomeIntegrationViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutinesTestRule(testDispatcher = StandardTestDispatcher())

    @Test
    fun `Data is loaded form server when local data source is empty`() = runTest{
        val remoteData = sampleMovies(1, 2, 3)
        val vm = createViewmodelWithFakes(remoteData = remoteData)

        vm.onUiReady()

        vm.state.test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(emptyList<Movie>()), awaitItem())
            assertEquals(Result.Success(remoteData),awaitItem())
        }
    }

    @Test
    fun `Data is loaded form local source`() = runTest{
        val localData = sampleMovies(1, 2, 3)
        val vm = createViewmodelWithFakes(localData = localData)

        vm.onUiReady()

        vm.state.test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(localData),awaitItem())
        }
    }

    private fun createViewmodelWithFakes(localData: List<Movie> = emptyList(), remoteData: List<Movie> = emptyList()): HomeViewModel{
        val moviesRepository = buildMoviesRepository(
            localData = localData,
            remoteData = remoteData
        )
        return HomeViewModel(
            fetchMoviesUseCase = FetchMoviesUseCase(moviesRepository),
            refreshMoviesUseCase = RefreshMoviesUseCase(moviesRepository)
        )
    }
}