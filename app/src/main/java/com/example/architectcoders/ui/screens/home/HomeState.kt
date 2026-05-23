package com.example.architectcoders.ui.screens.home

import android.Manifest
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.architectcoders.ui.common.PermissionRequestEffect
import com.example.architectcoders.ui.common.getRegion
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class HomeState(val scrollBehavior: TopAppBarScrollBehavior) {
    @Composable
    fun AskRegionEffect(onRegion: (String) -> Unit){
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        PermissionRequestEffect(permission = Manifest.permission.ACCESS_COARSE_LOCATION) { granted ->
            coroutineScope.launch {
                lateinit var region: String
                if (granted) {
                    region = context.getRegion()
                } else {
                    region = "US"
                }
                onRegion(region)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberHomeState(scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
): HomeState{
    return remember { HomeState(scrollBehavior) }
}