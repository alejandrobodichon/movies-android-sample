package com.example.data.di

import android.app.Application
import android.location.Geocoder
import androidx.room.Room
import com.example.data.BuildConfig
import com.example.data.database.MoviesDatabase
import com.example.data.database.MoviesRoomDataSource
import com.example.data.datasource.FusedLocationDataSource
import com.example.data.datasource.GeocoderDataSource
import com.example.data.datasource.RegionDataSourceImpl
import com.example.data.datasource.GeocoderDataSourceImpl
import com.example.data.remote.ApiKeyInterceptor
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
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) = Room.databaseBuilder(app, MoviesDatabase::class.java, "movies-db")
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()

    @Provides
    fun provideMoviesDao(db: MoviesDatabase) = db.moviesDao

    @Provides
    fun provideGeocoder(app: Application) = Geocoder(app)

    @Provides
    fun provideFusedLocationClient(app: Application) = LocationServices.getFusedLocationProviderClient(app)

}

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): ApiKeyInterceptor =
        ApiKeyInterceptor(BuildConfig.TMDB_API_KEY)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()

    @Provides
    @Singleton
    fun provideMoviesService(
        retrofit: Retrofit
    ): MoviesService =
        retrofit.create(MoviesService::class.java)
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
        impl: RegionDataSourceImpl
    ): RegionDataSource

    @Binds
    abstract fun bindLocationDataSource(
        impl: FusedLocationDataSource
    ): LocationDataSource

    @Binds
    abstract fun bindMoviesRepository(
        impl: MoviesRepository
    ): IMoviesRepository

    @Binds
    abstract fun bindGeocoderDataSource(
        impl: GeocoderDataSourceImpl
    ): GeocoderDataSource

}
