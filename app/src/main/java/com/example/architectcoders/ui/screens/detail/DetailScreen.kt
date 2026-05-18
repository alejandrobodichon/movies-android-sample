package com.example.architectcoders.ui.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.architectcoders.ui.screens.home.Screen
import com.example.architectcoders.data.Movie
import com.example.architectcoders.data.movies


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(movie: Movie, onBack: () -> Unit) {
    Screen {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(movie.title) },
                    navigationIcon = {
                        IconButton(onClick = onBack){
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    }
                    , scrollBehavior = scrollBehavior)
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        )
        { contentPadding ->
            Column(modifier = Modifier.padding(contentPadding)) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
   DetailScreen(movies.first(),{})
}