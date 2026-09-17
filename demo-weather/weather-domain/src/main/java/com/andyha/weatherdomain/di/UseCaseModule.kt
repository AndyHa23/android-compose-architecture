package com.andyha.weatherdomain.di

import com.andyha.weatherdomain.usecase.getCurrentLocationState.GetLocationStateUseCase
import com.andyha.weatherdomain.usecase.getCurrentLocationState.GetLocationStateUseCaseImpl
import com.andyha.weatherdomain.usecase.getCurrentWeather.GetCurrentWeatherUseCase
import com.andyha.weatherdomain.usecase.getCurrentWeather.GetCurrentWeatherUseCaseImpl
import com.andyha.weatherdomain.usecase.getDailyForecast.GetDailyForecastUseCase
import com.andyha.weatherdomain.usecase.getDailyForecast.GetDailyForecastUseCaseImpl
import com.andyha.weatherdomain.usecase.getHourlyForecast.GetHourlyForecastUseCase
import com.andyha.weatherdomain.usecase.getHourlyForecast.GetHourlyForecastUseCaseImpl
import com.andyha.weatherdomain.usecase.getLocationHistory.GetLocationHistoryUseCase
import com.andyha.weatherdomain.usecase.getLocationHistory.GetLocationHistoryUseCaseImpl
import com.andyha.weatherdomain.usecase.requestLocationUpdate.RequestLocationUpdateUseCaseImpl
import com.andyha.weatherdomain.usecase.requestLocationUpdate.RequestLocationUpdateUseCase
import com.andyha.weatherdomain.usecase.setSelectedLocationUseCase.SetSelectedLocationUseCase
import com.andyha.weatherdomain.usecase.setSelectedLocationUseCase.SetSelectedLocationUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    @ViewModelScoped
    abstract fun bindGetCurrentWeatherUseCase(impl: GetCurrentWeatherUseCaseImpl): GetCurrentWeatherUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetHourlyForecastUseCase(impl: GetHourlyForecastUseCaseImpl): GetHourlyForecastUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetDailyForecastUseCase(impl: GetDailyForecastUseCaseImpl): GetDailyForecastUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindRequestLocationUpdateUseCase(impl: RequestLocationUpdateUseCaseImpl): RequestLocationUpdateUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetLocationStateUseCase(impl: GetLocationStateUseCaseImpl): GetLocationStateUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetLocationHistoryUseCase(impl: GetLocationHistoryUseCaseImpl): GetLocationHistoryUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindSetSelectedLocationUseCase(impl: SetSelectedLocationUseCaseImpl): SetSelectedLocationUseCase
}