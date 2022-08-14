package com.amandaluz.marvelproject.data.network

import com.amandaluz.marvelproject.data.model.CharacterResponse
import com.amandaluz.marvelproject.data.model.Comics
import com.amandaluz.marvelproject.data.model.modelcomics.ComicsResponse
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("/v1/public/characters")
    suspend fun searchCharacter(
        @Query("nameStartsWith") nameStartsWith: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: Long
        ): CharacterResponse

    @GET("v1/public/characters/{id}/comics")
    suspend fun getComics(
        @Path("id") id: Long,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: Long
    ): ComicsResponse

    @GET("v1/public/characters/{id}/series")
    suspend fun getSeries(
        @Path("id") id: Long,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: Long
    ): ComicsResponse
}