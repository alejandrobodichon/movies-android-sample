package com.example.moviesexample.data.datasource

import android.location.Geocoder
import android.location.Location

interface RegionDataSource {
    suspend fun findLastRegion(): String

    suspend fun Location.toRegion(): String?
}