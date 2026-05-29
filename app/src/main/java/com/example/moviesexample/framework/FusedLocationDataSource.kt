package com.example.moviesexample.framework

import android.annotation.SuppressLint
import android.location.Location
import com.example.moviesexample.data.datasource.LocationDataSource
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class FusedLocationDataSource @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationDataSource {

    override suspend fun findLastLocation(): Location? = fusedLocationClient.lastLocation()

    @SuppressLint("MissingPermission")
    override suspend fun FusedLocationProviderClient.lastLocation(): Location? {
        return suspendCancellableCoroutine { continuation ->
            lastLocation.addOnSuccessListener { location ->
                continuation.resume(location)
            }.addOnFailureListener {
                continuation.resume(null)
            }
        }
    }
}