package com.andyha.musicdata.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andyha.musicdata.dao.SavedPlayListDao
import com.andyha.musicdomain.model.SavedPlayList

const val VERSION = 1
const val DATABASE_NAME = "Demo-Music"

@Database(
    version = VERSION,
    entities = [SavedPlayList::class],
    exportSchema = true,
)

@TypeConverters(MusicConverter::class)

abstract class MusicDatabase : RoomDatabase() {

    abstract fun getSavedPlayListDao(): SavedPlayListDao

    companion object {
        fun getInstance(
            context: Context,
        ): MusicDatabase {
            val builder = Room.databaseBuilder(context, MusicDatabase::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
            return builder.build()
        }
    }
}
