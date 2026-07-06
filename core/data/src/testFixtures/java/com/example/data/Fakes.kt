package com.example.data

import com.example.data.repository.MoviesRepository
import com.example.moviesexample.domain.datasource.MoviesLocalDataSource
import com.example.moviesexample.domain.datasource.MoviesRemoteDataSource
import com.example.moviesexample.domain.datasource.RegionDataSource
import com.example.moviesexample.domain.entity.Movie
import com.example.moviesexample.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

fun buildMoviesRepository(
    localData: List<Movie> = emptyList(),
    remoteData: List<Movie> = emptyList()
): IMoviesRepository {
    val remoteDataSource = FakeRemoteDataSource(remoteData)
    val localDataSource = FakeLocalDataSource(localData)
    val regionDataSource = FakeRegionDataSource()

    return MoviesRepository(remoteDataSource, regionDataSource, localDataSource)
}

class FakeLocalDataSource(
    initialMovies: List<Movie> = emptyList()
) : MoviesLocalDataSource {

    private val inMemoryMovies = MutableStateFlow(initialMovies)
    private val favoriteIds = MutableStateFlow<Set<Int>>(emptySet())

    override val movies: Flow<List<Movie>> =
        combine(inMemoryMovies, favoriteIds) { movies, favs ->
            movies.map { it.copy(favorite = it.id in favs) }
        }

    override fun findMovieById(id: Int): Flow<Movie?> =
        movies.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun saveMovies(movies: List<Movie>) {
        val current = inMemoryMovies.value.associateBy { it.id }.toMutableMap()
        movies.forEach { current[it.id] = it }
        inMemoryMovies.value = current.values.toList()
    }

    override suspend fun updateFavorite(movieId: Int, favorite: Boolean) {
        favoriteIds.value = if (favorite) favoriteIds.value + movieId
        else favoriteIds.value - movieId
    }
}

class FakeRemoteDataSource(private val movies: List<Movie> = emptyList()) : MoviesRemoteDataSource {
    override suspend fun fetchPopularMovies(findLastRegion: String): List<Movie> = movies

    override suspend fun findMovieById(id: Int): Movie =
        movies.first { it.id == id }
}

class FakeRegionDataSource(val region: String = "US") : RegionDataSource {

    override suspend fun findLastRegion(): String = region
}
