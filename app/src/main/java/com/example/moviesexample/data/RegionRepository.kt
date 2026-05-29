package com.example.moviesexample.data

import com.example.moviesexample.data.datasource.RegionDataSource
import javax.inject.Inject


class RegionRepository @Inject constructor(val geoCoderRegionDataSource: RegionDataSource) {

    suspend fun findLastRegion(): String = geoCoderRegionDataSource.findLastRegion()
}