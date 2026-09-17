package com.andyha.musicdomain.repository

import androidx.media3.common.MediaItem
import com.andyha.musicdomain.constants.Constants
import com.andyha.musicdomain.model.DataState
import kotlinx.coroutines.flow.StateFlow


interface MediaTreeRepository {

    val dataState: StateFlow<DataState>

    fun refresh()

    fun getRootItem(): MediaItem?

    fun getChildren(id: String): List<MediaItem>

    fun getItem(id: String): MediaItem?

    fun search(query: String): List<MediaItem>

    fun getParentId(mediaId: String, parentId: String = Constants.ROOT_ID): String?
}