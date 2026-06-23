package com.example.data.datasource

import com.example.moviesexample.domain.datasource.LocationDataSource
import com.example.moviesexample.domain.datasource.RegionDataSource
import com.example.moviesexample.domain.entity.Location
import javax.inject.Inject

class RegionDataSourceImpl @Inject constructor(
    private val geocoderDataSource: GeocoderDataSource,
    private val fusedLocationDataSource: LocationDataSource
) : RegionDataSource {

    override suspend fun findLastRegion(): String =
        fusedLocationDataSource.findLastLocation()?.toRegion() ?: DEFAULT_REGION

    private suspend fun Location.toRegion(): String? {
        val addresses = geocoderDataSource.getFromLocation(latitude, longitude, 1)
        return addresses.firstOrNull()?.countryCode
    }

    companion object{
        const val DEFAULT_REGION = "US"
    }
}


