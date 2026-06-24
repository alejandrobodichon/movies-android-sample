package com.example.moviesexample.domain.entity


sealed interface Result <out T>{
    data class Success<T>(val data: T): Result<T>
    data class Error(val appError: AppError): Result<Nothing>
    object Loading: Result<Nothing>
}