package com.example.moviesexample.di

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import androidx.room.Room
import com.example.moviesexample.BuildConfig
import com.example.moviesexample.framework.database.MoviesDatabase
import com.example.moviesexample.framework.FusedLocationDataSource
import com.example.moviesexample.framework.GeoCoderRegionDataSource
import com.example.moviesexample.data.datasource.LocationDataSource
import com.example.moviesexample.data.datasource.MoviesLocalDataSource
import com.example.moviesexample.data.datasource.MoviesRemoteDataSource
import com.example.moviesexample.framework.database.MoviesRoomDataSource
import com.example.moviesexample.framework.remote.MoviesServerDataSource
import com.example.moviesexample.data.datasource.RegionDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import com.example.moviesexample.framework.remote.MoviesClient
import com.example.moviesexample.framework.remote.MoviesService
import com.example.moviesexample.ui.screens.MovieDetailRoute
import com.google.android.gms.location.LocationServices
import dagger.Binds
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

    @Provides
    fun provideGeocoder(app: Application) = Geocoder(app)

    @Provides
    fun provideFusedLocationClient(app: Application) = LocationServices.getFusedLocationProviderClient(app)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMoviesLocalDataSource(
        impl: MoviesRoomDataSource
    ): MoviesLocalDataSource

    @Binds
    abstract fun bindMoviesRemoteDataSource(
        impl: MoviesServerDataSource
    ): MoviesRemoteDataSource

    @Binds
    abstract fun bindRegionDataSource(
        impl: GeoCoderRegionDataSource
    ): RegionDataSource

    @Binds
    abstract fun bindLocationDataSource(
        impl: FusedLocationDataSource
    ): LocationDataSource
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

