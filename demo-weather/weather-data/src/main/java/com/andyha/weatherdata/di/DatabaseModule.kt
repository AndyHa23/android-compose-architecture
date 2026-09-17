package com.andyha.weatherdata.di

import android.content.Context
import com.andyha.coredata.storage.preference.AppSharedPreference
import com.andyha.weatherdata.dao.LocationDetectedDao
import com.andyha.weatherdata.dao.WeatherDao
import com.andyha.weatherdata.database.AppDatabase
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
        prefs: AppSharedPreference,
    ): AppDatabase = AppDatabase.getInstance(context, prefs)

    @Singleton
    @Provides
    fun provideWeatherDao(db: AppDatabase): WeatherDao = db.getWeatherDao()

    @Singleton
    @Provides
    fun provideLocationDetectedDao(db: AppDatabase): LocationDetectedDao =
        db.getLocationDetectedDao()
}
