package com.amandaluz.marvelproject.data.repository.loginrepository.hawk

import com.amandaluz.marvelproject.core.ModuleHawk

class CharacterCacheRepositoryImpl(private val hawk: ModuleHawk) : CharacterCacheRepository{
    override fun <T> add(key: String, obj: T) {
        hawk.put(key, obj)
    }

    override fun delete(key: String) {
        if (hawk.contains(key))
            hawk.delete(key)
    }

    override fun <T> get(key: String): T = hawk.get(key)

}