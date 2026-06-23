package com.example.data.datasource

import android.annotation.SuppressLint
import android.location.Location as AndroidLocation
import com.example.moviesexample.domain.datasource.LocationDataSource
import com.example.moviesexample.domain.entity.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class FusedLocationDataSource @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationDataSource {

    override suspend fun findLastLocation(): Location? = fusedLocationClient.lastLocation()

    @SuppressLint("MissingPermission")
    suspend fun FusedLocationProviderClient.lastLocation(): Location? {
        return suspendCancellableCoroutine { continuation ->
            lastLocation.addOnSuccessListener { location ->
                continuation.resume(location.toDomainLocation())
            }.addOnFailureListener {
                continuation.resume(null)
            }
        }
    }


}

private fun AndroidLocation.toDomainLocation() = Location(
    latitude = latitude,
    longitude = longitude
)