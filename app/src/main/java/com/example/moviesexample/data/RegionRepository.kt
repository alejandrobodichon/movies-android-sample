package com.example.moviesexample.data

import com.example.moviesexample.data.datasource.RegionDataSource


class RegionRepository(val regionDataSource: RegionDataSource) {

    suspend fun findLastRegion(): String = regionDataSource.findLastRegion()
}