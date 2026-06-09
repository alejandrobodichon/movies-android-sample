package com.example.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.moviesexample.domain.entity.Movie
import com.example.ui.MeScaffold
import com.example.ui.Screen
import com.example.moviesexample.domain.entity.Result
import com.example.ui.model.MovieUiModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    vm: DetailViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val movieDetail by vm.movieDetail.collectAsStateWithLifecycle()
    val isFavorite by vm.isFavorite.collectAsStateWithLifecycle()
    val detailState = rememberDetailState()
    val movie = (movieDetail as? Result.Success)?.data


    Screen {
        val scrollBehavior = detailState.scrollBehavior
        MeScaffold(
            state = movieDetail,
            topBar = {
                TopAppBar(
                    title = { Text(movie?.title ?: "") },
                    navigationIcon = {
                        IconButton(onClick = onBack){
                            Icon(imageVector = Filled.ArrowBack, contentDescription = null)
                        }
                    }
                    , scrollBehavior = scrollBehavior)
            },
            floatingActionButton = {
                FavoriteButton(
                    isFavorite =  isFavorite ,
                    onClick = vm::onFavoriteClick
                )
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
    movie: MovieUiModel
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
private fun FavoriteButton(
    isFavorite:  Boolean,
    onClick: () -> Unit
) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            imageVector = if (isFavorite)
                Icons.Default.Favorite
            else
                Icons.Default.FavoriteBorder,
            contentDescription = null
        )
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