package com.amandaluz.marvelproject.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Comics(
    @SerializedName("available")
    val available: Int,
    @SerializedName("collectionURI")
    val collectionURI: String,
    @SerializedName("items")
    val items: List<ItemsComics>,
    @SerializedName("returned")
    val returned: Int
): Serializable
