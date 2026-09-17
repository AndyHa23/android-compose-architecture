package com.andyha.musicdomain.repository

import androidx.media3.common.MediaItem
import com.andyha.musicdomain.model.SavedPlayList
import kotlinx.coroutines.flow.StateFlow


interface SavedPlayListRepository {

    val savedPlayList: StateFlow<SavedPlayList?>

    fun savePlayList(playList: SavedPlayList)

    fun savePlayList(
        mediaItems: List<MediaItem>,
        playingItem: MediaItem,
        playingPosition: Long,
        repeatMode: Int,
        shuffleMode: Boolean,
    )
}