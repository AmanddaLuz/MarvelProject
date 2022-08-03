package com.amandaluz.marvelproject.data.model

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorites_table", primaryKeys = ["id", "email"])
@Parcelize
data class Favorites(
    @SerializedName("id")
    val id: Long,
    @SerializedName("comics")
    val comics: Comics,
    @SerializedName("description")
    val description: String,
    @SerializedName("events")
    val events: Events,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("resourceURI")
    val resourceURI: String,
    @SerializedName("series")
    val series: Series,
    @SerializedName("stories")
    val stories: Stories,
    @SerializedName("thumbnail")
    val thumbnail: Thumbnail,
    @SerializedName("urls")
    val urls: List<Url>,
    val email: String
): Parcelable

