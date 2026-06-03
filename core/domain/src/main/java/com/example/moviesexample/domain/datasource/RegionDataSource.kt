package com.example.moviesexample.domain.datasource


interface RegionDataSource {
    suspend fun findLastRegion(): String
}