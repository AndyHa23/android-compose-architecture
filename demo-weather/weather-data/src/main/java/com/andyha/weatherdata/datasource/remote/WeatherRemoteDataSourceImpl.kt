package com.andyha.weatherdata.datasource.remote

import com.andyha.weatherdata.networking.api.WeatherApi
import com.andyha.weatherdata.networking.transformer.toDailyForecast
import com.andyha.weatherdata.networking.transformer.toHourlyForecast
import com.andyha.weatherdata.networking.transformer.toWeatherModel
import com.andyha.weatherdomain.model.DailyForecast
import com.andyha.weatherdomain.model.HourlyForecastWithDay
import com.andyha.weatherdomain.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRemoteDataSource {
    override fun getCurrentWeather(lat: Double, lng: Double): Flow<Weather> =
        flow {
            weatherApi.getCurrentWeather("$lat,$lng").also {
                emit(it.toWeatherModel())
            }
        }

    override fun getHourlyForecast(lat: Double, lng: Double): Flow<List<HourlyForecastWithDay>> =
        flow {
            weatherApi.getHourlyForecast("$lat,$lng").also {
                emit(it.toHourlyForecast())
            }
        }

    override fun getDailyForecast(lat: Double, lng: Double): Flow<List<DailyForecast>> =
        flow {
            weatherApi.getDailyForecast("$lat,$lng").also {
                emit(it.toDailyForecast())
            }
        }
}