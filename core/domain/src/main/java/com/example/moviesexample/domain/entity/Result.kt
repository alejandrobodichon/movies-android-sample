package com.example.moviesexample.domain.entity

sealed interface Result<out T> {
    data class Success<out T>(val data: T): Result<T>
    data class Error(val appError: AppError): Result<Nothing>
    data object Loading: Result<Nothing>
}
