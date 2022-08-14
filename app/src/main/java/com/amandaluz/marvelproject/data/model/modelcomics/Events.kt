package com.amandaluz.marvelproject.data.model.modelcomics

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)