package com.andyha.musicdata.repository

import androidx.media3.common.MediaItem
import com.andyha.musicdata.datasource.local.LocalDataSource
import com.andyha.musicdomain.model.SavedMusicItem
import com.andyha.musicdomain.model.SavedPlayList
import com.andyha.musicdomain.repository.SavedPlayListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject


class SavedPlayListRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
) : SavedPlayListRepository {

    private val _savedPlayList = MutableStateFlow<SavedPlayList?>(null)
    override val savedPlayList = _savedPlayList.asStateFlow()

    init {
        getSavedPlayList()
    }

    override fun savePlayList(playList: SavedPlayList) {
        localDataSource.savePlayList(playList)
    }

    override fun savePlayList(
        mediaItems: List<MediaItem>,
        playingItem: MediaItem,
        playingPosition: Long,
        repeatMode: Int,
        shuffleMode: Boolean,
    ) {
        val savedPlayList = SavedPlayList(
            mediaItems.map { SavedMusicItem(it) },
            playingItem.mediaId,
            playingPosition,
            repeatMode,
            shuffleMode,
        )
        localDataSource.savePlayList(savedPlayList)
    }

    private fun getSavedPlayList() {
        localDataSource.getPlayList()?.let {
            Timber.d("getSavedPlayList: $it")
            _savedPlayList.value = it
        }
    }
}