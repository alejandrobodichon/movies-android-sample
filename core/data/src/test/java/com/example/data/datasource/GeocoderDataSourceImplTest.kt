package com.example.data.datasource

import android.location.Address
import android.location.Geocoder
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class GeocoderDataSourceImplTest {

    @Test
    fun `getFromLocation on modern sdk returns addresses from listener callback`() = runTest {
        val addresses = listOf(mock<Address>())
        val geocoder = mock<Geocoder> {
            doAnswer { invocation ->
                val listener = invocation.getArgument<Geocoder.GeocodeListener>(3)
                listener.onGeocode(addresses)
            }.whenever(it).getFromLocation(any(), any(), any(), any())
        }
        val dataSource = GeocoderDataSourceImpl(geocoder)

        val result = dataSource.getFromLocation(1.0, 2.0, 5)

        assertEquals(addresses, result)
    }

    @Test
    @Config(sdk = [28])
    fun `getFromLocation on legacy sdk returns addresses from blocking call`() = runTest {
        val addresses = listOf(mock<Address>())
        val geocoder = mock<Geocoder> {
            @Suppress("DEPRECATION")
            doReturn(addresses).whenever(it).getFromLocation(1.0, 2.0, 5)
        }
        val dataSource = GeocoderDataSourceImpl(geocoder)

        val result = dataSource.getFromLocation(1.0, 2.0, 5)

        assertEquals(addresses, result)
    }

    @Test
    @Config(sdk = [28])
    fun `getFromLocation on legacy sdk returns empty list when geocoder returns null`() = runTest {
        val geocoder = mock<Geocoder> {
            @Suppress("DEPRECATION")
            doReturn(null).whenever(it).getFromLocation(1.0, 2.0, 5)
        }
        val dataSource = GeocoderDataSourceImpl(geocoder)

        val result = dataSource.getFromLocation(1.0, 2.0, 5)

        assertEquals(emptyList<Address>(), result)
    }
}
