package com.example.ui

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviesexample.domain.entity.AppError
import com.example.moviesexample.domain.entity.Result
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class MeScaffoldTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `Loading state does not show error or content`() {
        composeRule.setContent {
            MeScaffold(state = Result.Loading) { _, _ ->
                Text("should not appear")
            }
        }

        composeRule.onNodeWithTag("ErrorTextContainer").assertDoesNotExist()
        composeRule.onNodeWithTag("ErrorIcon").assertDoesNotExist()
        composeRule.onNodeWithTag("ErrorText").assertDoesNotExist()
        composeRule.onNodeWithText("should not appear").assertDoesNotExist()
    }

    @Test
    fun `Success state shows content and no error`() {
        composeRule.setContent {
            MeScaffold(state = Result.Success("hello")) { _, data ->
                Text(text = data)
            }
        }

        composeRule.onNodeWithText("hello").assertIsDisplayed()
        composeRule.onNodeWithTag("ErrorTextContainer").assertDoesNotExist()
    }

    @Test
    fun `Error state shows error components`() {
        composeRule.setContent {
            MeScaffold(state = Result.Error(AppError.Network.NoInternet)) { _, _ -> }
        }

        composeRule.onNodeWithTag("ErrorTextContainer").assertIsDisplayed()
        composeRule.onNodeWithTag("ErrorIcon").assertIsDisplayed()
        composeRule.onNodeWithTag("ErrorText").assertIsDisplayed()
    }

    @Test
    fun `Error state does not show content`() {
        composeRule.setContent {
            MeScaffold(state = Result.Error(AppError.Unknown)) { _, _ ->
                Text("should not appear")
            }
        }

        composeRule.onNodeWithText("should not appear").assertDoesNotExist()
    }
}
