package com.amandaluz.marvelproject.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    val email: String,
    val name: String,
    val password: String,
    val photo: Int? = null,
    val favourites: List<Results>
)
