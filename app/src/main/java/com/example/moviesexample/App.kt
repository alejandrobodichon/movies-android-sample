package com.example.moviesexample

import android.app.Application
import androidx.room.Room
import com.example.data.database.MoviesDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application()