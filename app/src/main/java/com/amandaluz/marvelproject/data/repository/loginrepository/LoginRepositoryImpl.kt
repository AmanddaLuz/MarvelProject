package com.amandaluz.marvelproject.data.repository.loginrepository

import com.amandaluz.marvelproject.data.db.CharacterDAO
import com.amandaluz.marvelproject.data.model.User

class LoginRepositoryImpl(private val dao: CharacterDAO):LoginRepository {
    override suspend fun login(email: String, password: String): User? =
        dao.getValidUser(email, password)
}