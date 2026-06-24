package com.example.data.extension

import com.example.moviesexample.domain.entity.AppError
import com.example.moviesexample.domain.entity.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

fun Throwable.toAppError(): AppError = when (this) {
    is UnknownHostException -> AppError.Network.NoInternet
    is SocketTimeoutException -> AppError.Network.Timeout
    is IOException -> AppError.Network.NoInternet
    is HttpException -> AppError.Network.HttpError(code())
    else -> AppError.Unknown
}

fun <T> Flow<T>.stateAsResultIn(scope: CoroutineScope): StateFlow<Result<T>> =
    map<T, Result<T>> { Result.Success(it) }
        .catch {  throwable ->
            if (throwable is CancellationException) throw throwable
            emit(Result.Error(throwable.toAppError()))
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading
        )