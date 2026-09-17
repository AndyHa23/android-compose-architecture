package com.andyha.weatherdomain.repository

import com.andyha.weatherdomain.model.DailyForecast
import com.andyha.weatherdomain.model.HourlyForecastWithDay
import com.andyha.weatherdomain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository{
    fun getCurrentWeather(lat: Double, lng: Double): Flow<Weather>
    fun getHourlyForecast(lat: Double, lng: Double): Flow<List<HourlyForecastWithDay>>
    fun getDailyForecast(lat: Double, lng: Double): Flow<List<DailyForecast>>
}