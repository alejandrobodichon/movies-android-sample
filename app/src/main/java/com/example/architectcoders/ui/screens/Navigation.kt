package com.example.architectcoders.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.architectcoders.ui.screens.detail.DetailScreen
import com.example.architectcoders.ui.screens.detail.DetailViewModel
import com.example.architectcoders.ui.screens.home.HomeScreen
import kotlinx.serialization.Serializable

sealed class NavScreen(val route: String){
    data object Home: NavScreen("home")
    data object Detail: NavScreen("detail/{${NavArgs.MovieId.key}}"){
        fun createRoute(movieId: Int) = "detail/$movieId"
    }
}

enum class NavArgs(val key: String){
    MovieId("movieId")
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavScreen.Home.route
    ) {
        composable(NavScreen.Home.route) {
            HomeScreen(onClick = { movie ->
                navController.navigate(NavScreen.Detail.createRoute(movie.id))
            })
        }
        composable<Int>{

        }

        composable(NavScreen.Detail.route, arguments = listOf(navArgument(NavArgs.MovieId.key) {
            type = NavType.IntType
        })) { backstackEntry ->
            val movieId = requireNotNull(backstackEntry.arguments?.getInt(NavArgs.MovieId.key))
            DetailScreen(
                vm = viewModel { DetailViewModel(movieId) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}


// ruta sin argumentos
@Serializable
object HomeRoute

// ruta con argumentos
@Serializable
data class MovieDetailRoute(val movieId: Int)