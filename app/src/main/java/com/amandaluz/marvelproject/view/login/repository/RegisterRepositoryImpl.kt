package com.amandaluz.marvelproject.view.login.repository

import com.amandaluz.marvelproject.data.db.CharacterDAO
import com.amandaluz.marvelproject.data.model.User
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val dao: CharacterDAO): RegisterRepository {
    override suspend fun registerNewUser(user: User) {
        dao.createNewUser(user)
    }
}