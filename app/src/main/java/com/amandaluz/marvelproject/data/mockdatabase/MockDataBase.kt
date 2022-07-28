package com.amandaluz.marvelproject.data.mockdatabase

import com.amandaluz.marvelproject.data.model.User

object MockDataBase {
    private var user = hashSetOf<User>()

    init {
        mockUser()
    }

    private fun mockUser() =
        user.run {
            add(User("amandaluz@hotmail.com", "Amanda Luz", "12345678", null))
            add(User("emersonsoares@hotmail.com", "Emerson Soares", "12345678", null))
            add(User("hectorfortuna@hotmail.com", "Hector Fortuna", "12345678", null))
            add(User("fernandaoliveira@hotmail.com", "Fernanda Oliveira", "12345678",null))
        }

    @Throws(Throwable::class)
    fun mockLogin(email: String, password: String): User? {
        return this.user.firstOrNull { email == it.email && password == it.password }
            ?: throw IllegalArgumentException("email e/ou senha inválidos")
    }
}