package com.amandaluz.marvelproject.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ItemsEvents(
    @SerializedName("name")
    val name: String,
    @SerializedName("resourceURI")
    val resourceURI: String
): Serializable
