package com.example.ui

import com.example.core.ui.R
import com.example.moviesexample.domain.entity.AppError
import com.example.ui.model.UiText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UiExtensionTest {

    private fun assertResource(expectedRes: Int, error: AppError) {
        val result = error.toUiText()
        assertTrue(result is UiText.Resource)
        assertEquals(expectedRes, (result as UiText.Resource).resId)
    }

    @Test
    fun `NoInternet maps to error_no_internet`() =
        assertResource(R.string.error_no_internet, AppError.Network.NoInternet)

    @Test
    fun `Timeout maps to error_timeout`() =
        assertResource(R.string.error_timeout, AppError.Network.Timeout)

    @Test
    fun `ServerError maps to error_server`() =
        assertResource(R.string.error_server, AppError.Network.ServerError)

    @Test
    fun `HttpError maps to error_server`() =
        assertResource(R.string.error_server, AppError.Network.HttpError(500))

    @Test
    fun `PermissionDenied maps to error_location_permission`() =
        assertResource(R.string.error_location_permission, AppError.Location.PermissionDenied)

    @Test
    fun `Unavailable maps to error_location_unavailable`() =
        assertResource(R.string.error_location_unavailable, AppError.Location.Unavailable)

    @Test
    fun `GeocoderFailed maps to error_location_unavailable`() =
        assertResource(R.string.error_location_unavailable, AppError.Location.GeocoderFailed)

    @Test
    fun `Unknown maps to error_unknown`() =
        assertResource(R.string.error_unknown, AppError.Unknown)
}
