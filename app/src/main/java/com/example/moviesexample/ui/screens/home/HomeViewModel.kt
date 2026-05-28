package com.example.moviesexample.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesexample.data.Movie
import com.example.moviesexample.data.MoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.moviesexample.Result
import com.example.moviesexample.stateAsResultIn
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest


class HomeViewModel(repository: MoviesRepository): ViewModel(){

    private var uiReady = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<Result<List<Movie>>> = uiReady
        .filter{ it }
        .flatMapLatest { repository.movies }
        .stateAsResultIn(scope = viewModelScope)

    fun onUiReady() {
        uiReady.value = true
    }

}