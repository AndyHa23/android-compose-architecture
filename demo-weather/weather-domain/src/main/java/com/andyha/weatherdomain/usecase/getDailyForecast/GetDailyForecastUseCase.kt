package com.andyha.weatherdomain.usecase.getDailyForecast

import com.andyha.coredata.base.BaseUseCase
import com.andyha.weatherdomain.model.DailyForecast
import kotlinx.coroutines.flow.Flow


interface GetDailyForecastUseCase : BaseUseCase<Pair<Double, Double>, Flow<List<DailyForecast>>>