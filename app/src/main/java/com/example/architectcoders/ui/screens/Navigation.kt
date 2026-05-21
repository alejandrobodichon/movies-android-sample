package com.example.architectcoders.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.architectcoders.ui.screens.detail.DetailScreen
import com.example.architectcoders.ui.screens.detail.DetailViewModel
import com.example.architectcoders.ui.screens.home.HomeScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<HomeRoute> {
            HomeScreen(onClick = { movie ->
                navController.navigate(MovieDetailRoute(movie.id))
            })
        }
        composable<MovieDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<MovieDetailRoute>()
            DetailScreen(
                vm = viewModel { DetailViewModel(route.movieId) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}


@Serializable
data object HomeRoute

@Serializable
data class MovieDetailRoute(val movieId: Int)