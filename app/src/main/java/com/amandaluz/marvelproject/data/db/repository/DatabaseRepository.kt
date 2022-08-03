package com.amandaluz.marvelproject.data.db.repository

import androidx.lifecycle.LiveData
import com.amandaluz.marvelproject.data.model.Favorites
import com.amandaluz.marvelproject.data.model.Results

interface DatabaseRepository {

    suspend fun insertCharacter(result:Favorites)
    suspend fun insertFavorite(favorites: Favorites)
    fun getAllCharacters() : LiveData<List<Favorites>>
    suspend fun deleteCharacter(result: Favorites)
    suspend fun getFavouriteCharacter(characterId: Long): Favorites?
    suspend fun getFavouriteCharacterByUser(characterId: Long, email: String):Favorites?
    fun getAllCharactersByUser(email: String): LiveData<List<Favorites>>
}