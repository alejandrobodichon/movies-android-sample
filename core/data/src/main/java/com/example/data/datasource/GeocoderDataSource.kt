package com.example.data.datasource

import android.location.Address
import androidx.annotation.FloatRange
import androidx.annotation.IntRange

interface GeocoderDataSource {
    suspend fun getFromLocation(
        @FloatRange(from = -90.0, to = 90.0) latitude: Double,
        @FloatRange(from = -180.0, to = 180.0) longitude: Double,
        @IntRange maxResults: Int
    ): List<Address>
}