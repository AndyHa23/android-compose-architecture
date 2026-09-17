package com.andyha.musicdata.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andyha.musicdomain.model.SavedPlayList

@Dao
abstract class SavedPlayListDao: BaseDao<SavedPlayList>() {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPlayList(playList: SavedPlayList)

    @Query("SELECT * FROM ${SavedPlayList.TABLE_NAME}")
    abstract fun getSavedPlayList(): SavedPlayList?
}
