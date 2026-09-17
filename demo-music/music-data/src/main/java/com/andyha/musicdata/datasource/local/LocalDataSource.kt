package com.andyha.musicdata.datasource.local

import com.andyha.musicdata.dao.SavedPlayListDao
import com.andyha.musicdomain.model.SavedPlayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val savedPlayListDao: SavedPlayListDao,
) : LocalDataSource {

    override fun savePlayList(playList: SavedPlayList) {
        savedPlayListDao.insertPlayList(playList)
    }

    override fun getPlayList(): SavedPlayList? {
        return savedPlayListDao.getSavedPlayList()
    }
}

interface LocalDataSource {
    fun savePlayList(playList: SavedPlayList)
    fun getPlayList(): SavedPlayList?
}