package com.example.architectcoders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.architectcoders.ui.screens.detail.DetailScreen
import com.example.architectcoders.ui.screens.detail.DetailViewModel
import com.example.architectcoders.ui.screens.home.HomeScreen

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "home"
            ){
                composable("home"){
                    HomeScreen(onClick = { movie ->
                        navController.navigate("detail/${movie.id}")
                    })
                }
                composable("detail/{movieId}", arguments = listOf(navArgument("movieId"){
                    type = NavType.IntType
                })){ backstackEntry ->
                    val movieId = requireNotNull(backstackEntry.arguments?.getInt("movieId"))
                    DetailScreen(
                        vm = viewModel { DetailViewModel(movieId)},
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

