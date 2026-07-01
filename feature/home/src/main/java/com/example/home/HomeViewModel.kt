package com.example.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.extension.stateAsResultIn
import com.example.moviesexample.domain.entity.AppError
import com.example.moviesexample.domain.entity.Movie
import com.example.moviesexample.domain.usecase.FetchMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import com.example.moviesexample.domain.entity.Result
import com.example.moviesexample.domain.usecase.RefreshMoviesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(fetchMoviesUseCase: FetchMoviesUseCase,
    val refreshMoviesUseCase: RefreshMoviesUseCase
): ViewModel(){

    private var uiReady = MutableStateFlow(false)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _errorEvent = MutableSharedFlow<AppError>()
    val errorEvent: SharedFlow<AppError> = _errorEvent.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<Result<List<Movie>>> = uiReady
        .filter{ it }
        .flatMapLatest { fetchMoviesUseCase() }
        .stateAsResultIn(scope = viewModelScope)

    fun onUiReady() {
        uiReady.value = true
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            when (val result = refreshMoviesUseCase()) {
                is Result.Error -> _errorEvent.emit(result.appError)
                is Result.Success -> { }
                is Result.Loading -> { }
            }
            _isRefreshing.value = false
        }
    }

}