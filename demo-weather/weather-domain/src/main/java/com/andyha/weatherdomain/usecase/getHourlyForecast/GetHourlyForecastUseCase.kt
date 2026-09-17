package com.andyha.weatherdomain.usecase.getHourlyForecast

import com.andyha.coredata.base.BaseUseCase
import com.andyha.weatherdomain.model.HourlyForecastWithDay
import kotlinx.coroutines.flow.Flow


interface GetHourlyForecastUseCase: BaseUseCase<Pair<Double, Double>, Flow<List<HourlyForecastWithDay>>>