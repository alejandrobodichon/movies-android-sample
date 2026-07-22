package com.example.moviesexample.domain.datasource

import com.example.moviesexample.domain.entity.Location

interface LocationDataSource {
    suspend fun findLastLocation(): Location?

}
