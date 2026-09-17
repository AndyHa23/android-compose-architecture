package com.andyha.musicdata.di

import com.andyha.musicdata.repository.MediaTreeRepositoryImpl
import com.andyha.musicdata.repository.SavedPlayListRepositoryImpl
import com.andyha.musicdomain.repository.MediaTreeRepository
import com.andyha.musicdomain.repository.SavedPlayListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMediaItemTreeRepository(repository: MediaTreeRepositoryImpl): MediaTreeRepository

    @Singleton
    @Binds
    abstract fun bindSavedPlayListRepository(repository: SavedPlayListRepositoryImpl): SavedPlayListRepository
}
