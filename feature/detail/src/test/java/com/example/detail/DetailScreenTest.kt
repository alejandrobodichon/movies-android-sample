package com.example.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviesexample.domain.entity.AppError
import com.example.moviesexample.domain.entity.Result
import com.example.testing.sampleMovie
import com.example.ui.model.toUiModel
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class DetailScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val sampleUiModel = sampleMovie(1).toUiModel()

    @Test
    fun `Loading state shows no content and no error`() {
        composeRule.setContent {
            DetailScreen(
                state = Result.Loading,
                isFavorite = false,
                onFavoriteClick = {},
                onBack = {}
            )
        }

        composeRule.onNodeWithTag("Backdrop").assertDoesNotExist()
        composeRule.onNodeWithTag("ErrorTextContainer").assertDoesNotExist()
    }

    @Test
    fun `Error state shows error components`() {
        composeRule.setContent {
            DetailScreen(
                state = Result.Error(AppError.Network.NoInternet),
                isFavorite = false,
                onFavoriteClick = {},
                onBack = {}
            )
        }

        composeRule.onNodeWithTag("ErrorTextContainer").assertIsDisplayed()
        composeRule.onNodeWithTag("ErrorIcon").assertIsDisplayed()
        composeRule.onNodeWithTag("Backdrop").assertDoesNotExist()
    }

    @Test
    fun `Success state shows movie detail components`() {
        composeRule.setContent {
            DetailScreen(
                state = Result.Success(sampleUiModel),
                isFavorite = false,
                onFavoriteClick = {},
                onBack = {}
            )
        }

        composeRule.onNodeWithTag("Backdrop").assertExists()
        composeRule.onNodeWithTag("DetailTitle").assertExists()
        composeRule.onNodeWithTag("DetailDescription").assertExists()
        composeRule.onNodeWithTag("FloatingActionButton").assertIsDisplayed()
    }

    @Test
    fun `Success state shows favorite FAB`() {
        composeRule.setContent {
            DetailScreen(
                state = Result.Success(sampleUiModel),
                isFavorite = false,
                onFavoriteClick = {},
                onBack = {}
            )
        }

        composeRule.onNodeWithTag("FloatingActionButton").assertIsDisplayed()
        composeRule.onNodeWithTag("FavIcon", useUnmergedTree = true).assertExists()
    }

    @Test
    fun `Click on FAB triggers onFavoriteClick`() {
        var clicked = false
        composeRule.setContent {
            DetailScreen(
                state = Result.Success(sampleUiModel),
                isFavorite = false,
                onFavoriteClick = { clicked = true },
                onBack = {}
            )
        }

        composeRule.onNodeWithTag("FloatingActionButton").performClick()

        assertTrue(clicked)
    }
}
