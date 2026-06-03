package com.example.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesexample.domain.entity.Movie
import com.example.moviesexample.domain.entity.stateAsResultIn
import com.example.moviesexample.domain.usecase.FetchMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import com.example.moviesexample.domain.entity.Result

@HiltViewModel
class HomeViewModel @Inject constructor(fetchMoviesUseCase: FetchMoviesUseCase): ViewModel(){

    private var uiReady = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<Result<List<Movie>>> = uiReady
        .filter{ it }
        .flatMapLatest { fetchMoviesUseCase() }
        .stateAsResultIn(scope = viewModelScope)

    fun onUiReady() {
        uiReady.value = true
    }

}