package com.example.data.repository

import android.location.Address
import com.example.data.datasource.GeocoderDataSource
import com.example.data.datasource.RegionDataSourceImpl
import com.example.moviesexample.domain.datasource.LocationDataSource
import com.example.moviesexample.domain.datasource.RegionDataSource
import com.example.moviesexample.domain.entity.Location
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

class RegionRepositoryTest {

    lateinit var geoCoderRegionDataSource: RegionDataSource

    @Mock
    lateinit var fusedLocationDataSource: LocationDataSource

    @Mock
    lateinit var geocoder:  GeocoderDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        geoCoderRegionDataSource = RegionDataSourceImpl(geocoder,fusedLocationDataSource)
    }

    @Test
    fun `Find last region returns default when geocoder returns empty`() = runTest {
        //Before
        val address = mock<Address>()
        whenever(address.countryCode).doReturn("AR")
        whenever(fusedLocationDataSource.findLastLocation()).doReturn(Location(0.0,0.0))
        whenever(geocoder.getFromLocation(any(),any(),any()))
            .doReturn(listOf(address))

        //When
        val result: String = geoCoderRegionDataSource.findLastRegion()

        //Then
        assertEquals("AR", result)

    }

    @Test
    fun `findLastRegion returns default when geocoder returns empty`() = runTest {
        whenever(fusedLocationDataSource.findLastLocation())
            .doReturn(Location(0.0, 0.0))
        whenever(geocoder.getFromLocation(any(), any(), any()))
            .doReturn(emptyList())

        val result = geoCoderRegionDataSource.findLastRegion()

        assertEquals("US", result)
    }

    @Test
    fun `findLastRegion returns default when location is null`() = runTest {
        whenever(fusedLocationDataSource.findLastLocation())
            .doReturn(null)

        val result = geoCoderRegionDataSource.findLastRegion()

        assertEquals("US", result)
    }
}