package com.andyha.musicdata.di

import com.andyha.musicdata.datasource.local.LocalDataSource
import com.andyha.musicdata.datasource.local.LocalDataSourceImpl
import com.andyha.musicdata.datasource.remote.MusicDataSource
import com.andyha.musicdata.datasource.remote.MusicDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindMusicDatasource(musicDataSource: MusicDataSourceImpl): MusicDataSource

    @Singleton
    @Binds
    abstract fun bindLocalDatasource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource
}
