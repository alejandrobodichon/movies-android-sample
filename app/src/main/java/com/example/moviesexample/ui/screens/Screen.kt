package com.example.moviesexample.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moviesexample.ui.theme.MoviesExampleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(content: @Composable () -> Unit) {
    MoviesExampleTheme {
        Surface(
            modifier = Modifier.Companion.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            content = content
        )
    }
}