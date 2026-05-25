package com.example.architectcoders.ui.screens

import android.app.Application
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.architectcoders.data.LocationDataSource
import com.example.architectcoders.data.MoviesRepository
import com.example.architectcoders.data.RegionDataSource
import com.example.architectcoders.data.RegionRepository
import com.example.architectcoders.ui.screens.detail.DetailScreen
import com.example.architectcoders.ui.screens.detail.DetailViewModel
import com.example.architectcoders.ui.screens.home.HomeScreen
import com.example.architectcoders.ui.screens.home.HomeViewModel
import kotlinx.serialization.Serializable

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val applicationContext = LocalContext.current.applicationContext
    val moviesRepository = MoviesRepository(
        regionRepository = RegionRepository(regionDataSource = RegionDataSource(
            app = applicationContext as Application,
            locationDataSource = LocationDataSource(app = applicationContext)
        ))
    )
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<HomeRoute> {
            HomeScreen(onClick = { movie ->
                navController.navigate(MovieDetailRoute(movie.id))
            },
                vm = viewModel { HomeViewModel(repository = moviesRepository)})
        }
        composable<MovieDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<MovieDetailRoute>()
            DetailScreen(
                vm = viewModel { DetailViewModel(route.movieId, moviesRepository) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}


@Serializable
data object HomeRoute

@Serializable
data class MovieDetailRoute(val movieId: Int)