package com.example.architectcoders.data

import com.example.architectcoders.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create


object MoviesClient {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(::apiKeyAsQuery)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    private val json = Json {
        ignoreUnknownKeys = true
    }

    val instance = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create<MoviesService>()

    val i = SyncRequest.Builder("").retries(11).build()
}

private fun apiKeyAsQuery(chain: Interceptor.Chain) = chain.proceed(
    chain.request().let { request ->
        request.newBuilder()
            .url(request.url
                .newBuilder()
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build()
            )
            .header("Accept-Encoding", "identity") // deshabilita gzip
            .build()
    }
)

@JvmInline
value class Seconds(val value: Long)

class SyncRequest private constructor(
    val url: String,
    val retries: Int,
    val interval: Long?,
    val tag: String?
) {
    class Builder(private val url: String) {
        private var retries: Int = 3
        private var interval: Long? = null
        private var tag: String? = null

        fun retries(value: Int) = apply {
            require(value > 0) { "Retries debe ser mayor a 0, recibido: $value" }
            require(value <= 10) { "Retries no puede ser mayor a 10, recibido: $value" }
            retries = value
        }

        fun interval(minutes: Long) = apply {
            require(minutes >= 15) { "WorkManager no permite intervalos menores a 15 minutos, recibido: $minutes" }
            interval = minutes
        }

        fun tag(value: String) = apply {
            require(value.isNotBlank()) { "El tag no puede estar vacío" }
            require(value.length <= 50) { "El tag no puede superar 50 caracteres" }
            tag = value
        }

        fun build(): SyncRequest {
            require(url.isNotBlank()) { "La URL no puede estar vacía" }
            require(url.startsWith("https")) { "La URL debe ser https" }
            // validaciones que cruzan varios campos
            requireNotNull(interval) { "Debe especificar un intervalo de sincronización" }

            return SyncRequest(url, retries, interval, tag)
        }
    }
}
