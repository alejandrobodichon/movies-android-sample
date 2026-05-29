package com.example.moviesexample.framework

import android.location.Geocoder
import android.location.Location
import com.example.moviesexample.data.datasource.LocationDataSource
import com.example.moviesexample.data.datasource.RegionDataSource
import com.example.moviesexample.ui.common.getFromLocationCompat
import javax.inject.Inject

class GeoCoderRegionDataSource @Inject constructor(
    private val geocoder: Geocoder,
    private val fusedLocationDataSource: LocationDataSource
) : RegionDataSource {

    override suspend fun findLastRegion(): String =
        fusedLocationDataSource.findLastLocation()?.toRegion() ?: DEFAULT_REGION

    override suspend fun Location.toRegion(): String? {
        val addresses = geocoder.getFromLocationCompat(latitude, longitude, 1)
        return addresses.firstOrNull()?.countryCode
    }

    companion object{
        const val DEFAULT_REGION = "US"
    }
}


