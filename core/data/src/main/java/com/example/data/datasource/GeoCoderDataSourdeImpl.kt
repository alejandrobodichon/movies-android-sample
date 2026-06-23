package com.example.data.datasource

import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume

class GeocoderDataSourceImpl @Inject constructor(
    private val geocoder: Geocoder
) : GeocoderDataSource {

    override suspend fun getFromLocation(
        latitude: Double,
        longitude: Double,
        maxResults: Int
    ): List<Address> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        suspendCancellableCoroutine { continuation ->
            geocoder.getFromLocation(latitude, longitude, maxResults) {
                continuation.resume(it)
            }
        }
    } else {
        withContext(Dispatchers.IO) {
            @Suppress("DEPRECATION")
            geocoder.getFromLocation(latitude, longitude, maxResults) ?: emptyList()
        }
    }
}