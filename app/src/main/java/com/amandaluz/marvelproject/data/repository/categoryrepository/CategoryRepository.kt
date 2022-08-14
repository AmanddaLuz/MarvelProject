package com.amandaluz.marvelproject.data.repository.categoryrepository

import com.amandaluz.marvelproject.data.model.modelcomics.ComicsResponse

interface CategoryRepository {
    suspend fun getComics(
        apikey: String,
        hash: String,
        ts: Long,
        id: Long
    ): ComicsResponse

    suspend fun getSeries(
        apikey: String,
        hash: String,
        ts: Long,
        id: Long
    ): ComicsResponse
}