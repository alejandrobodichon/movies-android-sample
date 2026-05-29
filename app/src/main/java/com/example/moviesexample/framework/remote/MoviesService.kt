package com.example.moviesexample.framework.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("discover/movie?sort_by=popularity.desc")
    suspend fun getPopularMovies(@Query("region") region: String): RemoteResult

    @GET("movie/{id}")
    suspend fun fetchMovieById(@Path("id") id: Int): RemoteMovie

}