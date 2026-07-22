package com.example.moviesexample.domain.usecase

import com.example.moviesexample.domain.repository.IMoviesRepository
import javax.inject.Inject
import com.example.moviesexample.domain.entity.Result

class RefreshMoviesUseCase @Inject constructor(
    private val repository: IMoviesRepository
) {
    suspend operator fun invoke(): Result<Unit> =
        repository.refresh()
}
