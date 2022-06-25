package com.amandaluz.marvelproject.data.db.converters

import androidx.room.TypeConverter
import com.amandaluz.marvelproject.data.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class Converters {

    @TypeConverter
    fun fromThumbnail(thumbnail: Thumbnail): String = Gson().toJson(thumbnail)

    @TypeConverter
    fun toThumbnail(string: String): Thumbnail = Gson().fromJson(string, Thumbnail::class.java)

    @TypeConverter
    fun fromComics(comics: Comics): String = Gson().toJson(comics)

    @TypeConverter
    fun toComics(string: String): Comics = Gson().fromJson(string, Comics::class.java)

    @TypeConverter
    fun fromSeries(series: Series): String = Gson().toJson(series)

    @TypeConverter
    fun toSeries(string: String): Series = Gson().fromJson(string, Series::class.java)

    @TypeConverter
    fun fromStories(stories: Stories): String = Gson().toJson(stories)

    @TypeConverter
    fun toStories(string: String): Stories = Gson().fromJson(string, Stories::class.java)

    @TypeConverter
    fun fromEvents(events: Events): String = Gson().toJson(events)

    @TypeConverter
    fun toEvents(string: String): Events = Gson().fromJson(string, Events::class.java)

    @TypeConverter
    fun fromListUrls(urls: List<Url>): String = Gson().toJson(urls)

    @TypeConverter
    fun toListUrls(data: String?): List<Url?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object :
            TypeToken<List<Url?>?>() {}.type
        return Gson().fromJson<List<Url?>>(data, listType)
    }

}