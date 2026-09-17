package com.andyha.weatherdomain.usecase.getHourlyForecast

import com.andyha.weatherdomain.model.HourlyForecastWithDay
import com.andyha.weatherdomain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetHourlyForecastUseCaseImpl @Inject constructor(
    private val weatherRepository: WeatherRepository
): GetHourlyForecastUseCase {
    override fun invoke(params: Pair<Double, Double>): Flow<List<HourlyForecastWithDay>> {
        return weatherRepository.getHourlyForecast(params.first, params.second)
    }
}