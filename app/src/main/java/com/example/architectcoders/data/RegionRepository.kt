package com.example.architectcoders.data

import com.example.architectcoders.data.datasource.RegionDataSource


class RegionRepository(val regionDataSource: RegionDataSource) {

    suspend fun findLastRegion(): String = regionDataSource.findLastRegion()
}