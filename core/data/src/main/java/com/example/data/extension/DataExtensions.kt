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

fun <T> Flow<T>.asResult(): Flow<Result<T>> =
    map<T, Result<T>> { Result.Success(it) }
        .catch { throwable ->
            if (throwable is CancellationException) throw throwable
            emit(Result.Error(throwable.toAppError()))
        }

fun <T> Flow<T>.stateAsResultIn(scope: CoroutineScope): StateFlow<Result<T>> =
    asResult()
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = Result.Loading
        )

fun <T> Result<T>.getOrNull(): T? = (this as? Result.Success)?.data

inline fun <T, R> Result<T>.mapResult(transform: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Error -> Result.Error(appError)
    Result.Loading -> Result.Loading
}

private const val STOP_TIMEOUT_MILLIS = 5000L
