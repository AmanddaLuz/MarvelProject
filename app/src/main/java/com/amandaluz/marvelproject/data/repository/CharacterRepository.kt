package com.amandaluz.marvelproject.data.repository

import com.amandaluz.marvelproject.data.model.CharacterResponse

interface CharacterRepository {
    suspend fun getCharacters(apikey: String, hash: String, ts: Long): CharacterResponse

    suspend fun searchCharacter(nameStartsWith: String, apikey: String, hash: String, ts: Long): CharacterResponse
}