package com.amandaluz.marvelproject.data.model.modelcomics

data class Characters(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)