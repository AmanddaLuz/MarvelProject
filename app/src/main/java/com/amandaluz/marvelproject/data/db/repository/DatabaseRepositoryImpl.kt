package com.amandaluz.marvelproject.data.db.repository

import androidx.lifecycle.LiveData
import com.amandaluz.marvelproject.data.db.CharacterDAO
import com.amandaluz.marvelproject.data.model.Favorites
import com.amandaluz.marvelproject.data.model.Results

class DatabaseRepositoryImpl(private val dao: CharacterDAO):DatabaseRepository {
    override suspend fun insertCharacter(result: Favorites) {
        TODO("Not yet implemented")
    }
//    override suspend fun insertCharacter(result: Favorites) =
//        dao.insertCharacter(result)

    override suspend fun insertFavorite(favorites: Favorites) =
        dao.insertCharacter(favorites)

    override fun getAllCharacters(): LiveData<List<Favorites>> {
        TODO("Not yet implemented")
    }

//    override fun getAllCharacters(): LiveData<List<Favorites>> =
//        dao.getAllCharacters()

    override suspend fun deleteCharacter(result: Favorites) =
        dao.deleteCharacter(result)

    override suspend fun getFavouriteCharacter(characterId: Long): Favorites? =
        dao.getFavoriteCharacter(characterId)

    override suspend fun getFavouriteCharacterByUser(characterId: Long, email: String): Favorites? =
        dao.getFavoriteCharacterByUser(characterId, email)


    override fun getAllCharactersByUser(email: String): LiveData<List<Favorites>> =
        dao.getAllCharacterByUser(email)

}