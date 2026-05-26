package com.example.moviesexample.ui.screens

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.moviesexample.App
import com.example.moviesexample.data.datasource.LocationDataSource
import com.example.moviesexample.data.MoviesRepository
import com.example.moviesexample.data.RegionRepository
import com.example.moviesexample.data.datasource.MoviesLocalDataSource
import com.example.moviesexample.data.datasource.RegionDataSource
import com.example.moviesexample.data.datasource.MoviesRemoteDataSource
import com.example.moviesexample.ui.screens.detail.DetailScreen
import com.example.moviesexample.ui.screens.detail.DetailViewModel
import com.example.moviesexample.ui.screens.home.HomeScreen
import com.example.moviesexample.ui.screens.home.HomeViewModel
import kotlinx.serialization.Serializable

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val app = LocalContext.current.applicationContext
    val moviesRepository = MoviesRepository(
        remoteDataSource = MoviesRemoteDataSource(),
        regionRepository = RegionRepository(
            RegionDataSource(
                app = app as Application,
                locationDataSource = LocationDataSource(app = app)
            )
        ),
        localDataSource = MoviesLocalDataSource(
            moviesDao = (app as App).db.moviesDao
        )
        )
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<HomeRoute> {
            HomeScreen(
                onClick = { movie ->
                    navController.navigate(MovieDetailRoute(movie.id))
                },
                vm = viewModel { HomeViewModel(repository = moviesRepository) })
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