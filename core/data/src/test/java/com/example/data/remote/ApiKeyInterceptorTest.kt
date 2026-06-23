package com.example.data.remote

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ApiKeyInterceptorTest {

    private val apiKey = "test_api_key_123"
    private val interceptor = ApiKeyInterceptor(apiKey)

    private fun buildChain(url: String): Interceptor.Chain {
        val request = Request.Builder().url(url).build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .build()
        val chain = mock<Interceptor.Chain>()
        whenever(chain.request()).thenReturn(request)
        whenever(chain.proceed(any())).thenReturn(response)
        return chain
    }

    @Test
    fun `intercept adds api_key query parameter to request`() {
        val chain = buildChain("https://api.themoviedb.org/3/discover/movie")
        val captor = argumentCaptor<Request>()

        interceptor.intercept(chain)

        verify(chain).proceed(captor.capture())
        assertEquals(apiKey, captor.firstValue.url.queryParameter("api_key"))
    }

    @Test
    fun `intercept adds Accept-Encoding identity header`() {
        val chain = buildChain("https://api.themoviedb.org/3/discover/movie")
        val captor = argumentCaptor<Request>()

        interceptor.intercept(chain)

        verify(chain).proceed(captor.capture())
        assertEquals("identity", captor.firstValue.header("Accept-Encoding"))
    }

    @Test
    fun `intercept preserves original URL path`() {
        val chain = buildChain("https://api.themoviedb.org/3/movie/123")
        val captor = argumentCaptor<Request>()

        interceptor.intercept(chain)

        verify(chain).proceed(captor.capture())
        assertEquals("/3/movie/123", captor.firstValue.url.encodedPath)
    }

    @Test
    fun `intercept returns response from chain proceed`() {
        val chain = buildChain("https://api.themoviedb.org/3/discover/movie")

        val response = interceptor.intercept(chain)

        assertEquals(200, response.code)
    }
}
