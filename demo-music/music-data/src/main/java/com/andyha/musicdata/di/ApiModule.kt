package com.andyha.musicdata.di

import com.andyha.corenetwork.qualifier.MusicService
import com.andyha.musicdata.networking.api.MusicApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun bindMusicApi(@MusicService retrofit: Retrofit): MusicApi =
        retrofit.create(MusicApi::class.java)
}