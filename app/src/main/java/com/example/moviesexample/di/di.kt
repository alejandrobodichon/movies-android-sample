package com.example.moviesexample.di

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import androidx.room.Room
import com.example.moviesexample.BuildConfig
import com.example.moviesexample.data.database.MoviesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import com.example.moviesexample.data.remote.MoviesClient
import com.example.moviesexample.data.remote.MoviesService
import com.example.moviesexample.ui.screens.MovieDetailRoute
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    @Singleton
    @Named("apiKey")
    fun provideApiKey() = BuildConfig.TMDB_API_KEY

    @Provides
    @Singleton
    fun provideDatabase(app: Application) = Room.databaseBuilder(app, MoviesDatabase::class.java, "movies-db").build()

    @Provides
    fun provideMoviesDao(db: MoviesDatabase) = db.moviesDao

    @Provides
    @Singleton
    fun provideMoviesService(): MoviesService = MoviesClient.instance

}

@Module
@InstallIn(ViewModelComponent::class)
object DetailViewModelModule{

    @Provides
    @ViewModelScoped
    fun provideMovieId(savedStateHandle: SavedStateHandle): Int {
        return checkNotNull(savedStateHandle.toRoute<MovieDetailRoute>().movieId)
    }
}

