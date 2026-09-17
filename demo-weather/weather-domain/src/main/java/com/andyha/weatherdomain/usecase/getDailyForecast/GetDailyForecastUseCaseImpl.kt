package com.andyha.weatherdomain.usecase.getDailyForecast

import com.andyha.weatherdomain.model.DailyForecast
import com.andyha.weatherdomain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetDailyForecastUseCaseImpl @Inject constructor(
    private val weatherRepository: WeatherRepository
) : GetDailyForecastUseCase {
    override fun invoke(params: Pair<Double, Double>): Flow<List<DailyForecast>> {
        return weatherRepository.getDailyForecast(params.first, params.second)
    }
}