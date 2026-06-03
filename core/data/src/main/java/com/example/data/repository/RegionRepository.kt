package com.example.data.repository

import com.example.moviesexample.domain.datasource.RegionDataSource
import com.example.moviesexample.domain.repository.IRegionRepository
import javax.inject.Inject

class RegionRepository @Inject constructor(private val geoCoderRegionDataSource: RegionDataSource) :
    IRegionRepository {

    override suspend fun findLastRegion(): String = geoCoderRegionDataSource.findLastRegion()
}