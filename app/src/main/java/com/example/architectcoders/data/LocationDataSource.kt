package com.example.architectcoders.data

import android.app.Application
import com.example.architectcoders.ui.common.lastLocation
import com.google.android.gms.location.LocationServices

class LocationDataSource(app: Application) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(app)

    suspend fun findLastLocation() = fusedLocationClient.lastLocation()

}