package com.amandaluz.marvelproject.data.repository

import com.amandaluz.marvelproject.data.model.CharacterResponse

class CharacterRepositoryMock(): CharacterRepository {
    override suspend fun getCharacters(apikey: String, hash: String, ts: Long, limit: Int, offset: Int): CharacterResponse =
        mockCharacter()

    override suspend fun searchCharacter(
        nameStartsWith: String,
        apikey: String,
        hash: String,
        ts: Long
    ): CharacterResponse {
        TODO("Not yet implemented")
    }
}