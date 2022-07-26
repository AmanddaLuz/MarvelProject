package com.amandaluz.marvelproject.data.repository.loginrepository

import com.amandaluz.marvelproject.data.model.User

interface LoginRepository {
    suspend fun login(email: String, password: String): User?
}