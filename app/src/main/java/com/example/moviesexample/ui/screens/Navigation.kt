package com.example.moviesexample.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviesexample.ui.screens.detail.DetailScreen
import com.example.moviesexample.ui.screens.home.HomeScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<HomeRoute> {
            HomeScreen(
                onClick = { movie ->
                    navController.navigate(MovieDetailRoute(movie.id))
                }
            )
        }
        composable<MovieDetailRoute> {
            DetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}


@Serializable
data object HomeRoute

@Serializable
data class MovieDetailRoute(val movieId: Int)