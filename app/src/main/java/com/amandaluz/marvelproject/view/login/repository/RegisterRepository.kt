package com.amandaluz.marvelproject.view.login.repository

import com.amandaluz.marvelproject.data.model.User

interface RegisterRepository {
    suspend fun registerNewUser(user: User)
}