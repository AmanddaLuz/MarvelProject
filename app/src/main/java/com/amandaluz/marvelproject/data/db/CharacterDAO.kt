package com.amandaluz.marvelproject.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amandaluz.marvelproject.data.model.Results

@Dao
interface CharacterDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(result: Results)

    @Query("SELECT * FROM results_table")
    fun getAllCharacters(): LiveData<List<Results>>

    @Delete
    suspend fun deleteCharacter(result: Results)
}