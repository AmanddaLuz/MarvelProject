package com.amandaluz.marvelproject.data.model.modelcomics

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Thumbnail(
    val extension: String,
    val path: String,
    val width: Int,
    val height: Int
): Parcelable{
    val aspectRatio: Float get() = 1f
}