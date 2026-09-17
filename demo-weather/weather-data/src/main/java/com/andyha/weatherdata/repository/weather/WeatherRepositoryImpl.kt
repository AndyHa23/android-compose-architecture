package com.andyha.weatherdata.repository.weather

import com.andyha.weatherdata.datasource.remote.WeatherRemoteDataSource
import com.andyha.weatherdomain.model.DailyForecast
import com.andyha.weatherdomain.model.HourlyForecastWithDay
import com.andyha.weatherdomain.model.Weather
import com.andyha.weatherdomain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class WeatherRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {
    override fun getCurrentWeather(lat: Double, lng: Double): Flow<Weather> {
        return weatherRemoteDataSource.getCurrentWeather(lat, lng)
    }

    override fun getHourlyForecast(lat: Double, lng: Double): Flow<List<HourlyForecastWithDay>> {
        return weatherRemoteDataSource.getHourlyForecast(lat, lng)
    }

    override fun getDailyForecast(lat: Double, lng: Double): Flow<List<DailyForecast>> {
        return weatherRemoteDataSource.getDailyForecast(lat, lng)
    }
}