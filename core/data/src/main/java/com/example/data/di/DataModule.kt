package com.example.data.di

import android.app.Application
import android.location.Geocoder
import androidx.room.Room
import com.example.data.BuildConfig
import com.example.data.database.MoviesDatabase
import com.example.data.database.MoviesRoomDataSource
import com.example.data.datasource.FusedLocationDataSource
import com.example.data.datasource.GeoCoderRegionDataSource
import com.example.data.remote.MoviesClient
import com.example.data.remote.MoviesServerDataSource
import com.example.data.remote.MoviesService
import com.example.data.repository.MoviesRepository
import com.example.moviesexample.domain.datasource.LocationDataSource
import com.example.moviesexample.domain.datasource.MoviesLocalDataSource
import com.example.moviesexample.domain.datasource.MoviesRemoteDataSource
import com.example.moviesexample.domain.datasource.RegionDataSource
import com.example.moviesexample.domain.repository.IMoviesRepository
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

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

    @Binds
    abstract fun bindMoviesRepository(
        impl: MoviesRepository
    ): IMoviesRepository
}
