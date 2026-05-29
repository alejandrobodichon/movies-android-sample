package com.example.moviesexample.data.datasource

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient

interface LocationDataSource {
    suspend fun findLastLocation(): Location?

    @SuppressLint("MissingPermission")
    suspend fun FusedLocationProviderClient.lastLocation(): Location?
}