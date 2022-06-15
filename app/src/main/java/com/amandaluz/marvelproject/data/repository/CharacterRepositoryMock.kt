package com.amandaluz.marvelproject.data.repository

import com.amandaluz.marvelproject.data.model.CharacterResponse

class CharacterRepositoryMock(): CharacterRepository {
    override suspend fun getCharacters(apikey: String, hash: String, ts: Long): CharacterResponse =
        mockCharacter()
}