package com.example.moviesexample.data.datasource

import android.app.Application
import android.location.Geocoder
import android.location.Location
import com.example.moviesexample.ui.common.getFromLocationCompat

class RegionDataSource(
    app: Application,
    private val locationDataSource: LocationDataSource
) {
    private val geocoder = Geocoder(app)

    suspend fun findLastRegion(): String =
        locationDataSource.findLastLocation()?.toRegion() ?: DEFAULT_REGION

    suspend fun Location.toRegion(): String? {
        val addresses = geocoder.getFromLocationCompat(latitude, longitude, 1)
        return addresses.firstOrNull()?.countryCode
    }

    companion object{
        const val DEFAULT_REGION = "US"
    }
}


