package com.amandaluz.marvelproject.data.repository.loginrepository

import com.amandaluz.marvelproject.data.db.CharacterDAO
import com.amandaluz.marvelproject.data.model.User
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val dao: CharacterDAO):LoginRepository {
    override suspend fun login(email: String, password: String): User? =
        dao.getValidUser(email, password)
}