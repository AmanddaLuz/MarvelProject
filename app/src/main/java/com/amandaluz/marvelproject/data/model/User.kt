package com.amandaluz.marvelproject.data.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    val email: String,
    val name: String,
    val password: String,
    val photo: Uri?
): Parcelable
