package com.example.architectcoders.ui.screens.detail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle

@OptIn(ExperimentalMaterial3Api::class)
class DetailState(
    val scrollBehavior: TopAppBarScrollBehavior,
    val snackBarHostState: SnackbarHostState
) {
    @Composable
    fun ShowMessageEffect(message: String?, onMessageShown: () -> Unit) {
        LaunchedEffect(message) {
            message?.let {
                snackBarHostState.currentSnackbarData?.dismiss()
                snackBarHostState.showSnackbar(message)
                onMessageShown()
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberDetailState(
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): DetailState {
    return remember { DetailState(scrollBehavior, snackbarHostState) }
}