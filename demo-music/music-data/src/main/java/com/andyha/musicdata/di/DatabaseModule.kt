package com.andyha.musicdata.di

import com.andyha.musicdata.dao.SavedPlayListDao
import android.content.Context
import com.andyha.musicdata.database.MusicDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): MusicDatabase =
        MusicDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideSavedPlayListDao(db: MusicDatabase): SavedPlayListDao = db.getSavedPlayListDao()
}
