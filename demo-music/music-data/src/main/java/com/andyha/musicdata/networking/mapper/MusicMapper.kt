package com.andyha.musicdata.networking.mapper

import com.andyha.musicdata.networking.response.MusicResponse
import com.andyha.musicdomain.model.Music
import javax.inject.Inject


class MusicMapperImpl @Inject constructor() : MusicMapper {

    override fun MusicResponse.toMusicModel(): List<Music> {
        return music?.let { list ->
            val result = mutableListOf<Music>()
            for (item in list) {
                if (item.id == null || item.source == null) continue
                result.add(
                    Music(
                        id = item.id!!,
                        title = item.title ?: "",
                        album = item.album ?: "",
                        artist = item.artist ?: "",
                        genre = item.genre ?: "",
                        source = item.source!!,
                        image = item.image ?: "",
                        trackNumber = item.trackNumber ?: 0,
                        totalTrackCount = item.totalTrackCount ?: 0,
                        duration = item.duration ?: 0,
                        site = item.site ?: ""
                    )
                )
            }
            return result
        } ?: listOf()
    }
}

interface MusicMapper {
    fun MusicResponse.toMusicModel(): List<Music>
}