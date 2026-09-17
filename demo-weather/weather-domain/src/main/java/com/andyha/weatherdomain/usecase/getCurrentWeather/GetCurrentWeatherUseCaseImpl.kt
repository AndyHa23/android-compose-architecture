package com.andyha.weatherdomain.usecase.getCurrentWeather

import com.andyha.weatherdomain.model.Weather
import com.andyha.weatherdomain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCurrentWeatherUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository
) : GetCurrentWeatherUseCase {
    override fun invoke(params: Pair<Double, Double>): Flow<Weather> {
        return repository.getCurrentWeather(params.first, params.second)
    }
}