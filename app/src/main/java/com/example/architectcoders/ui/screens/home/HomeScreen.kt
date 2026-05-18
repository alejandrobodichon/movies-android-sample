package com.example.architectcoders.ui.screens.home

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.architectcoders.R
import com.example.architectcoders.ui.common.PermissionRequestEffect
import com.example.architectcoders.ui.common.getRegion
import com.example.architectcoders.ui.theme.ArchitectCodersTheme
import com.example.architectcoders.data.Movie
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onClick: (Movie) -> Unit,
               vm: HomeViewModel = viewModel()) {
    val appName = stringResource(R.string.app_name)
    var appBarTitle by remember { mutableStateOf(appName) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val state = vm.state

    PermissionRequestEffect(permission = Manifest.permission.ACCESS_COARSE_LOCATION) { granted ->
        if(granted){
            coroutineScope.launch {
                appBarTitle = "$appName (${context.getRegion()})"
            }
        } else {
            appBarTitle = appName + "permission denied"
        }
        vm.onUiReady()
    }

    Screen {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        Scaffold(
            topBar = {
                TopAppBar({
                    Text(appBarTitle)
                }, scrollBehavior = scrollBehavior)
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        )
        { contentPadding ->
            if (state.loading) {
                Box(Modifier.fillMaxSize()
                    .padding(contentPadding),
                    contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(120.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = contentPadding
            ) {
                items(state.movies) {
                    MovieItem(movie = it, onClick = onClick)
                }
            }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onClick: (Movie) -> Unit) {
    Column(modifier = Modifier
        .clickable { onClick(movie) }
            ){
        AsyncImage(
            model = movie.poster,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2 / 3f)
                .clip(MaterialTheme.shapes.medium)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 1,
            text = movie.title
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HomeScreen(onClick = { })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(content: @Composable () -> Unit) {
    ArchitectCodersTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            content = content
        )
    }
}