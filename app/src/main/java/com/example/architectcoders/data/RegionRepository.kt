package com.example.architectcoders.data


class RegionRepository(val regionDataSource: RegionDataSource) {

    suspend fun findLastRegion(): String = regionDataSource.findLastRegion()
}