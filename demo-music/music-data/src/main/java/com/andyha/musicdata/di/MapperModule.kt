package com.andyha.musicdata.di

import com.andyha.musicdata.networking.mapper.MusicMapper
import com.andyha.musicdata.networking.mapper.MusicMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
abstract class MapperModule {
    @Singleton
    @Binds
    abstract fun bindMusicMapper(musicMapperImpl: MusicMapperImpl): MusicMapper
}