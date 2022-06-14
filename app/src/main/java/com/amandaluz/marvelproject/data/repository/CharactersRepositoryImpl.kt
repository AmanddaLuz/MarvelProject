package com.amandaluz.marvelproject.data.repository

import com.amandaluz.marvelproject.data.model.CharacterResponse
import com.amandaluz.marvelproject.data.network.Service

class CharactersRepositoryImpl(private val api: Service): CharacterRepository {
    override suspend fun getCharacters(apikey: String, hash: String, ts: Long): CharacterResponse =
        api.getCharacters(apikey, hash, ts)
}