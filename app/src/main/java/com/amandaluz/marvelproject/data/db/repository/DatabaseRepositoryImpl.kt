package com.amandaluz.marvelproject.data.db.repository

import androidx.lifecycle.LiveData
import com.amandaluz.marvelproject.data.db.CharacterDAO
import com.amandaluz.marvelproject.data.model.Results

class DatabaseRepositoryImpl(private val dao: CharacterDAO): DatabaseRepository {
    override suspend fun insertCharacter(result: Results) = dao.insertCharacter(result)

    override fun getAllCharacters(): LiveData<List<Results>> = dao.getAllCharacters()

    override suspend fun deleteCharacter(result: Results) = dao.deleteCharacter(result)
}