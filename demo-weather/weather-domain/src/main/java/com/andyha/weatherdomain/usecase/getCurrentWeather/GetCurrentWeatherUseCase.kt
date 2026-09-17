package com.andyha.weatherdomain.usecase.getCurrentWeather

import com.andyha.coredata.base.BaseUseCase
import com.andyha.weatherdomain.model.Weather
import kotlinx.coroutines.flow.Flow


interface GetCurrentWeatherUseCase : BaseUseCase<Pair<Double, Double>, Flow<Weather>>