package com.example.data.extension

import com.example.moviesexample.domain.entity.AppError
import org.junit.Assert.assertEquals
import org.junit.Test
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class DataExtensionsTest {

    @Test
    fun `UnknownHostException maps to NoInternet`() {
        assertEquals(AppError.Network.NoInternet, UnknownHostException().toAppError())
    }

    @Test
    fun `SocketTimeoutException maps to Timeout`() {
        assertEquals(AppError.Network.Timeout, SocketTimeoutException().toAppError())
    }

    @Test
    fun `IOException maps to NoInternet`() {
        assertEquals(AppError.Network.NoInternet, IOException().toAppError())
    }

    @Test
    fun `HttpException maps to HttpError with code`() {
        val httpException = HttpException(Response.error<Unit>(404, "".toResponseBody(null)))

        val result = httpException.toAppError()

        assertEquals(AppError.Network.HttpError(404), result)
    }

    @Test
    fun `Unknown exception maps to Unknown`() {
        assertEquals(AppError.Unknown, RuntimeException().toAppError())
    }
}
