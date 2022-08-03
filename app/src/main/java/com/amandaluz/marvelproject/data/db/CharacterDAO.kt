package com.amandaluz.marvelproject.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amandaluz.marvelproject.data.model.Favorites
import com.amandaluz.marvelproject.data.model.Results
import com.amandaluz.marvelproject.data.model.User

@Dao
interface CharacterDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacter(result: Favorites)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createNewUser(user: User)

    @Delete
    suspend fun deleteCharacter(result: Favorites)

    @Query("SELECT * FROM favorites_table")
    fun getAllCharacters(): LiveData<List<Favorites>>

    @Query("SELECT * FROM favorites_table WHERE email = :email")
    fun getAllCharacterByUser(email: String): LiveData<List<Favorites>>

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password")
    suspend fun getValidUser(email: String, password: String): User?

    @Query("SELECT * FROM favorites_table WHERE id= :characterId")
    suspend fun getFavoriteCharacter(characterId: Long): Favorites?

    @Query("SELECT * FROM favorites_table WHERE id = :characterId AND email = :email")
    suspend fun getFavoriteCharacterByUser(characterId: Long, email: String): Favorites?


}