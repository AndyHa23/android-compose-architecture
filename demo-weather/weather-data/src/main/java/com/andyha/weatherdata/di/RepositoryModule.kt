package com.andyha.weatherdata.di

import com.andyha.weatherdata.repository.location.LocationRepositoryImpl
import com.andyha.weatherdata.repository.weather.WeatherRepositoryImpl
import com.andyha.weatherdomain.repository.LocationRepository
import com.andyha.weatherdomain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository
}