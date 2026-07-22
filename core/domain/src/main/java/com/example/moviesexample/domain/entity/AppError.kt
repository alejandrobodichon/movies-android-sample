package com.example.moviesexample.domain.entity

sealed interface AppError {

    sealed interface Network : AppError {
        data object NoInternet : Network
        data object Timeout : Network
        data object ServerError : Network
        data class HttpError(val code: Int) : Network
    }

    sealed interface Location : AppError {
        data object PermissionDenied : Location
        data object Unavailable : Location
        data object GeocoderFailed : Location
    }

    // Catch-all
    data object Unknown : AppError
}
