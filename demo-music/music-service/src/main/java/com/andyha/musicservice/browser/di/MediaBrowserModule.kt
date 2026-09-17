package com.andyha.musicservice.browser.di

import com.andyha.musicservice.browser.MediaPlayerBrowser
import android.content.Context
import com.andyha.corenetwork.di.IoDispatcher
import com.andyha.corenetwork.di.MainDispatcher
import com.andyha.musicdomain.repository.SavedPlayListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MediaBrowserModule {

    @Provides
    @Singleton
    fun provideMediaBrowser(
        @ApplicationContext context: Context,
        @MainDispatcher mainDispatcher: CoroutineDispatcher,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        savedPlayListRepository: SavedPlayListRepository,
    ): MediaPlayerBrowser =
        MediaPlayerBrowser(context, mainDispatcher, ioDispatcher, savedPlayListRepository)
}
