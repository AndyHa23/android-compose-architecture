package com.andyha.musicdata.datasource.remote

import com.andyha.musicdata.networking.api.MusicApi
import com.andyha.musicdata.networking.mapper.MusicMapper
import com.andyha.musicdomain.model.Music
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicDataSourceImpl @Inject constructor(
    private val musicApi: MusicApi,
    private val musicMapper: MusicMapper,
) : MusicDataSource {

    override fun getRemoteMusic(): Flow<List<Music>> {
        return flow {
            val response = musicApi.getMusicList() ?: error("Music catalog response was empty")
            Timber.d(response.toString())
            with(musicMapper) {
                emit(response.toMusicModel())
            }
        }
    }
}

interface MusicDataSource {
     fun getRemoteMusic(): Flow<List<Music>>
}
