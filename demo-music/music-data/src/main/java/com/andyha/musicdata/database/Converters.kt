package com.andyha.musicdata.database

import com.andyha.musicdomain.model.SavedMusicItem
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MusicConverter {

    @TypeConverter
    fun toDbValue(mediaItems: List<SavedMusicItem>): String = gson.toJson(mediaItems)

    @TypeConverter
    fun fromDbToValue(data: String?): List<SavedMusicItem> =
        data?.let { gson.fromJson(it, listType) } ?: listOf()

    companion object {
        private val gson = Gson()
        private val listType = object : TypeToken<List<SavedMusicItem>>() {}.type
    }
}
