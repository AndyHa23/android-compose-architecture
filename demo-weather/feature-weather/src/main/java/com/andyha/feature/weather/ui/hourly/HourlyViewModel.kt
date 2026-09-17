package com.andyha.feature.weather.ui.hourly

import com.andyha.corenetwork.di.IoDispatcher
import com.andyha.coreui.manager.ApiErrorHandler
import com.andyha.feature.weather.model.Section
import com.andyha.weatherdomain.model.HourlyForecast
import com.andyha.weatherdomain.usecase.getCurrentLocationState.GetLocationStateUseCase
import com.andyha.weatherdomain.usecase.getHourlyForecast.GetHourlyForecastUseCase
import com.andyha.weatherdomain.usecase.requestLocationUpdate.RequestLocationUpdateUseCase
import com.andyha.feature.weather.base.BaseWeatherViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@HiltViewModel
class HourlyViewModel @Inject constructor(
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase,
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

    private val _hourlyForecast = MutableStateFlow<List<Section<Long, HourlyForecast>>>(listOf())
    val hourlyForecast = _hourlyForecast.asStateFlow()

    fun getHourlyForecast() {
        currentLocation?.let { currentLocation ->
            getHourlyForecastUseCase.invoke(Pair(currentLocation.lat, currentLocation.lng))
                .map { it.map { Section(it.dayTimeStamp, it.forecast) } }
                .justLaunch { _hourlyForecast.emit(it) }
        } ?: requestLocationUpdate()
    }

    override fun onLocationDetected() {
        getHourlyForecast()
    }
}