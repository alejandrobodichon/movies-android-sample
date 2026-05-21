package com.example.architectcoders.data

class MoviesRepository(private val moviesService: MoviesService = MoviesClient.instance) {

    suspend fun fetchPopularMovies(region: String): List<Movie> {
        val movies = moviesService
            .getPopularMovies(region)
            .results
            .map{it.toDomainModel()}
        return movies
    }

    suspend fun findMovieById(id: Int): Movie {
        val movie = moviesService
            .fetchMovieById(id)
            .toDomainModel()
        return movie
    }

    private fun RemoteMovie.toDomainModel() = Movie(
        poster = "https://image.tmdb.org/t/p/w500$posterPath",
        title = title,
        id = id,
        releaseDate = releaseDate,
        backdrop = backdropPath?.let{"https://image.tmdb.org/t/p/w780$it"},
        overview = overview,
        popularity = popularity,
        voteAverage = voteAverage,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle
    )
}
