package com.amandaluz.marvelproject.data.repository.loginrepository

import com.amandaluz.marvelproject.data.mockdatabase.MockDataBase
import com.amandaluz.marvelproject.data.model.User

class LoginRepositoryMock: LoginRepository {
    override suspend fun login(email: String, password: String): User? =
        MockDataBase.mockLogin(email, password)
}