package com.example.data.database

import androidx.room.Embedded
import com.example.moviesexample.domain.entity.Movie

data class DbMovieWithFavorite(
    @Embedded val movie: DbMovie,
    val favorite: Boolean
) {
    fun toDomainMovie(): Movie {
        return Movie(
            id = movie.id,
            poster = movie.poster,
            title = movie.title,
            releaseDate = movie.releaseDate,
            overview = movie.overview,
            backdrop = movie.backdrop,
            originalTitle = movie.originalTitle,
            originalLanguage = movie.originalLanguage,
            popularity = movie.popularity,
            voteAverage = movie.voteAverage,
            favorite = favorite
        )
    }
}
