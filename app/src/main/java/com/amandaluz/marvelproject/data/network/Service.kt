package com.amandaluz.marvelproject.data.network

import com.amandaluz.marvelproject.data.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("/v1/public/characters")
    suspend fun getCharacters(
        @Query("offset") offset: Int? = 100,
        @Query("limit") limit: Int? = 50,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: Long
    ): CharacterResponse
}