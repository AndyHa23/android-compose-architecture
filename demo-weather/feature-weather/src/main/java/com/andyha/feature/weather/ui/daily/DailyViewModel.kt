package com.andyha.feature.weather.ui.daily

import com.andyha.corenetwork.di.IoDispatcher
import com.andyha.coreui.manager.ApiErrorHandler
import com.andyha.weatherdomain.model.DailyForecast
import com.andyha.weatherdomain.usecase.getCurrentLocationState.GetLocationStateUseCase
import com.andyha.weatherdomain.usecase.getDailyForecast.GetDailyForecastUseCase
import com.andyha.weatherdomain.usecase.requestLocationUpdate.RequestLocationUpdateUseCase
import com.andyha.feature.weather.base.BaseWeatherViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class DailyViewModel @Inject constructor(
    private val getDailyForecastUseCase: GetDailyForecastUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val errorHandler: ApiErrorHandler,
    private val getLocationStateUseCase: GetLocationStateUseCase,
    private val requestLocationUpdateUseCase: RequestLocationUpdateUseCase,
) : BaseWeatherViewModel(
    ioDispatcher,
    errorHandler,
    getLocationStateUseCase,
    requestLocationUpdateUseCase,
) {

    private val _dailyForecast = MutableStateFlow<List<DailyForecast>>(listOf())
    val dailyForecast = _dailyForecast.asStateFlow()

    fun getDailyForecast() {
        currentLocation?.let { currentLocation ->
            getDailyForecastUseCase
                .invoke(Pair(currentLocation.lat, currentLocation.lng))
                .justLaunch { _dailyForecast.emit(it) }
        } ?: requestLocationUpdate()
    }

    override fun onLocationDetected() {
        getDailyForecast()
    }
}