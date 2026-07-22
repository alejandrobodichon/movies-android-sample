package com.example.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviesexample.domain.entity.AppError
import com.example.moviesexample.domain.entity.Movie
import com.example.moviesexample.domain.entity.Result
import com.example.testing.sampleMovies
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `Loading state shows no grid and no error`() {
        composeTestRule.setContent {
            HomeScreen(
                onClick = {},
                onRefresh = {},
                state = Result.Loading,
                isRefreshing = false,
                snackbarHostState = SnackbarHostState()
            )
        }

        composeTestRule.onNodeWithTag("MoviesGrid").assertDoesNotExist()
        composeTestRule.onNodeWithTag("ErrorTextContainer").assertDoesNotExist()
    }

    @Test
    fun `Error state shows error components`() {
        composeTestRule.setContent {
            HomeScreen(
                onClick = {},
                onRefresh = {},
                state = Result.Error(AppError.Network.NoInternet),
                isRefreshing = false,
                snackbarHostState = SnackbarHostState()
            )
        }

        composeTestRule.onNodeWithTag("ErrorTextContainer").assertIsDisplayed()
        composeTestRule.onNodeWithTag("ErrorIcon").assertIsDisplayed()
        composeTestRule.onNodeWithTag("MoviesGrid").assertDoesNotExist()
    }

    @Test
    fun `Success state shows movies grid`() {
        val movies = sampleMovies(1, 2, 3)
        composeTestRule.setContent {
            HomeScreen(
                onClick = {},
                onRefresh = {},
                state = Result.Success(movies),
                isRefreshing = false,
                snackbarHostState = SnackbarHostState()
            )
        }

        composeTestRule.onNodeWithTag("MoviesGrid").assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("MovieItem").assertCountEquals(3)
    }

    @Test
    fun `Success state shows movie titles`() {
        val movies = sampleMovies(1, 2)
        composeTestRule.setContent {
            HomeScreen(
                onClick = {},
                onRefresh = {},
                state = Result.Success(movies),
                isRefreshing = false,
                snackbarHostState = SnackbarHostState()
            )
        }

        composeTestRule.onAllNodesWithTag("MovieTitle", useUnmergedTree = true).assertCountEquals(2)
    }

    @Test
    fun `Success state empty shows grid with no items`() {
        composeTestRule.setContent {
            HomeScreen(
                onClick = {},
                onRefresh = {},
                state = Result.Success(emptyList()),
                isRefreshing = false,
                snackbarHostState = SnackbarHostState()
            )
        }

        composeTestRule.onNodeWithTag("MoviesGrid").assertExists()
        composeTestRule.onAllNodesWithTag("MovieItem").assertCountEquals(0)
    }

    @Test
    fun `Click on movie item triggers onClick`() {
        val movies = sampleMovies(1)
        var clickedMovie: Movie? = null
        composeTestRule.setContent {
            HomeScreen(
                onClick = { clickedMovie = it },
                onRefresh = {},
                state = Result.Success(movies),
                isRefreshing = false,
                snackbarHostState = SnackbarHostState()
            )
        }

        composeTestRule.onNodeWithTag("MovieItem").performClick()

        assertEquals(movies[0], clickedMovie)
    }
}
