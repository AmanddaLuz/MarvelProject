package com.amandaluz.marvelproject.data.repository

import com.amandaluz.marvelproject.data.model.CharacterResponse
import com.google.gson.Gson

fun mockCharacter() = Gson().fromJson("{}", CharacterResponse::class.java)

