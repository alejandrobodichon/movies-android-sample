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
    fun `Data is loaded form server when local data source is empty`() = runTest{
        val remoteData = sampleMovies(1, 2, 3)
        val vm = createViewmodelWithFakes(id = 2, localData = remoteData)
        vm.movieState.test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(remoteData[1].toUiModel()), awaitItem())
        }
    }

    private fun createViewmodelWithFakes(id: Int = 1, localData: List<Movie> = emptyList(), remoteData: List<Movie> = emptyList()): DetailViewModel{
        val moviesRepository = buildMoviesRepository(
            localData = localData,
            remoteData = remoteData
        )
        return DetailViewModel(
            movieId = id ,
            findMovieByIdUseCase = FindMovieByIdUseCase(moviesRepository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(moviesRepository)
        )
    }
}