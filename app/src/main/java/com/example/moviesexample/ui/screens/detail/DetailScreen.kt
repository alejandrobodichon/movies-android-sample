package com.example.moviesexample.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.moviesexample.domain.Movie
import com.example.moviesexample.ui.common.MeScaffold
import com.example.moviesexample.ui.screens.Screen
import com.example.moviesexample.Result


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    vm: DetailViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by vm.state.collectAsState()
    val detailState = rememberDetailState()

    Screen {
        val scrollBehavior = detailState.scrollBehavior
        MeScaffold(
            state = state,
            topBar = {
                TopAppBar(
                    title = { Text((state as? Result.Success)?.data?.title ?: "") },
                    navigationIcon = {
                        IconButton(onClick = onBack){
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    }
                    , scrollBehavior = scrollBehavior)
            },
            floatingActionButton = {
                FloatingActionButton(onClick = vm::onFavoriteClick) {
                    Icon(imageVector =
                        if((state as? Result.Success)?.data?.favorite == true)
                            Icons.Default.Favorite
                        else
                            Icons.Default.FavoriteBorder,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        )
        { contentPadding, movie ->
            MovieDetail(modifier = Modifier.padding(contentPadding),
                movie = movie)

        }
    }
}

@Composable
private fun MovieDetail(
    modifier: Modifier = Modifier,
    movie: Movie
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        AsyncImage(
            model = movie.backdrop,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2 / 3f)
                .clip(MaterialTheme.shapes.medium)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding( 16.dp),
            textAlign = TextAlign.Start,
            text = movie.overview
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding( 16.dp),
            text = buildAnnotatedString {
                Property("Original language", movie.originalLanguage)
                Property("Original title", movie.originalTitle)
                Property("Release date", movie.releaseDate)
                Property("Popularity", movie.popularity.toString())
                Property("Vote average", movie.voteAverage.toString(), true)
            })
    }
}

@Composable
private fun AnnotatedString.Builder.Property(name: String, value: String, end: Boolean = false){
    withStyle(ParagraphStyle(lineHeight = 18.sp)){
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)){
            append("$name: ")
        }
        append(value)
        if(!end){
            appendLine()
        }
    }

}