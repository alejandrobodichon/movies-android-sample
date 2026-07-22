package com.example.moviesexample.di

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import com.example.moviesexample.MovieDetailRoute
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DetailViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideMovieId(savedStateHandle: SavedStateHandle): Int {
        return checkNotNull(savedStateHandle.toRoute<MovieDetailRoute>().movieId)
    }
}
