package com.example.moviesexample.domain.repository

interface IRegionRepository {
    suspend fun findLastRegion(): String
}