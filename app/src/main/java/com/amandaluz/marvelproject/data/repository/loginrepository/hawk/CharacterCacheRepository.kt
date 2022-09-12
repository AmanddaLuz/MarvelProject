package com.amandaluz.marvelproject.data.repository.loginrepository.hawk

interface CharacterCacheRepository {
    fun <T> add(key: String, obj: T)
    fun delete(key: String)
    fun <T> get(key: String): T
}