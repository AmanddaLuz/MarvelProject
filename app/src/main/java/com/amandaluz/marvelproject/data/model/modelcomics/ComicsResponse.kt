package com.amandaluz.marvelproject.data.model.modelcomics

data class ComicsResponse(
    val attributionHTML: String,
    val attributionText: String,
    val code: Int,
    val copyright: String,
    val `data`: Data,
    val etag: String,
    val status: String
)