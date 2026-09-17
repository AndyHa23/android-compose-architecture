package com.andyha.weatherdata.di

import com.andyha.corenetwork.qualifier.WeatherService
import com.andyha.weatherdata.networking.api.WeatherApi
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
    fun bindWeatherApi(@WeatherService retrofit: Retrofit): com.andyha.weatherdata.networking.api.WeatherApi =
        retrofit.create(WeatherApi::class.java)
}