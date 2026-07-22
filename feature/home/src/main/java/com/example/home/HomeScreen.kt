package com.example.home

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import com.example.core.ui.R
import com.example.moviesexample.domain.entity.Movie
import com.example.moviesexample.domain.entity.Result
import com.example.ui.MeScaffold
import com.example.ui.PermissionRequestEffect
import com.example.ui.Screen
import com.example.ui.toUiText

@Composable
fun HomeScreen(
    onClick: (Movie) -> Unit,
    vm: HomeViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    PermissionRequestEffect(permission = Manifest.permission.ACCESS_COARSE_LOCATION) {
        vm.onUiReady()
    }
    val context = LocalContext.current

    val state by vm.state.collectAsStateWithLifecycle()
    val isRefreshing by vm.isRefreshing.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        vm.errorEvent
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect { error ->
                snackbarHostState.showSnackbar(error.toUiText().asString(context))
            }
    }
    HomeScreen(onClick, { vm.refresh() }, state, isRefreshing, snackbarHostState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onClick: (Movie) -> Unit,
    onRefresh: () -> Unit,
    state: Result<List<Movie>>,
    isRefreshing: Boolean,
    snackbarHostState: SnackbarHostState
) {
    val appName = stringResource(R.string.app_name)
    var appBarTitle by remember { mutableStateOf(appName) }
    val homeState = rememberHomeState()

    Screen {
        MeScaffold(
            state = state,
            topBar = {
                TopAppBar({
                    Text(appBarTitle)
                }, scrollBehavior = homeState.scrollBehavior)
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.nestedScroll(homeState.scrollBehavior.nestedScrollConnection)
        ) { contentPadding, movies ->
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { onRefresh() },
                modifier = Modifier
                    .testTag("PullToRefreshBox")
                    .padding(contentPadding)
            ) {
                LazyVerticalGrid(
                    modifier = Modifier.testTag("MoviesGrid"),
                    columns = GridCells.Adaptive(120.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(items = movies) {
                        MovieItem(movie = it, onClick = onClick)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onClick: (Movie) -> Unit) {
    Column(
        modifier = Modifier
            .testTag("MovieItem")
            .clickable { onClick(movie) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(MOVIE_POSTER_ASPECT_RATIO)
                .clip(MaterialTheme.shapes.medium)
        ) {
            AsyncImage(
                modifier = Modifier.testTag("MoviePoster"),
                model = movie.poster,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop
            )
            Icon(
                modifier = Modifier
                    .testTag("FavIcon")
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(14.dp),
                tint = MaterialTheme.colorScheme.inverseOnSurface,
                imageVector =
                if (movie.favorite)
                    Icons.Default.Favorite
                else
                    Icons.Default.FavoriteBorder,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier
                .testTag("MovieTitle")
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 1,
            text = movie.title
        )
    }
}

private const val MOVIE_POSTER_ASPECT_RATIO = 2 / 3f
