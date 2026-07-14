package com.example.data.datasource

import android.location.Location as AndroidLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FusedLocationDataSourceTest {

    @Test
    fun `findLastLocation returns mapped location on success`() = runTest {
        val androidLocation = mock<AndroidLocation> {
            on { latitude } doReturn 1.0
            on { longitude } doReturn 2.0
        }
        val client = mockClientWithLastLocation(success = androidLocation)
        val dataSource = FusedLocationDataSource(client)

        val result = dataSource.findLastLocation()

        assertEquals(1.0, result?.latitude)
        assertEquals(2.0, result?.longitude)
    }

    @Test
    fun `findLastLocation returns null on failure`() = runTest {
        val client = mockClientWithLastLocation(success = null, fails = true)
        val dataSource = FusedLocationDataSource(client)

        val result = dataSource.findLastLocation()

        assertNull(result)
    }

    private fun mockClientWithLastLocation(
        success: AndroidLocation?,
        fails: Boolean = false
    ): FusedLocationProviderClient {
        val task = mock<Task<AndroidLocation>> {
            doAnswer { invocation ->
                if (!fails) invocation.getArgument<OnSuccessListener<AndroidLocation>>(0).onSuccess(success)
                invocation.mock
            }.whenever(it).addOnSuccessListener(any())
            doAnswer { invocation ->
                if (fails) invocation.getArgument<OnFailureListener>(0).onFailure(Exception("boom"))
                invocation.mock
            }.whenever(it).addOnFailureListener(any())
        }
        return mock {
            on { lastLocation } doReturn task
        }
    }
}
