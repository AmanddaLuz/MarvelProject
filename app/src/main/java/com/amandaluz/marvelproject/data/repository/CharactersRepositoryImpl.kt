package com.amandaluz.marvelproject.data.repository

import com.amandaluz.marvelproject.data.model.CharacterResponse
import com.amandaluz.marvelproject.data.network.Service
import com.amandaluz.marvelproject.util.apikey

class CharactersRepositoryImpl(private val api: Service): CharacterRepository {
    override suspend fun getCharacters(apikey: String, hash: String, ts: Long, limit: Int, offset: Int): CharacterResponse =
        api.getCharacters(apikey = apikey, hash = hash, ts = ts, limit = 50, offset = offset)

    override suspend fun searchCharacter(
        nameStartsWith: String,
        apikey: String,
        hash: String,
        ts: Long
    ): CharacterResponse =
        api.searchCharacter(nameStartsWith, apikey, hash, ts)
}