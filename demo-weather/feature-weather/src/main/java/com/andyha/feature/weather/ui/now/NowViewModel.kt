package com.andyha.feature.weather.ui.now

import com.andyha.corenetwork.di.IoDispatcher
import com.andyha.coreui.manager.ApiErrorHandler
import com.andyha.weathercommon.model.CurrentWeatherData
import com.andyha.weathercommon.model.toCurrentWeatherData
import com.andyha.weatherdomain.usecase.getCurrentLocationState.GetLocationStateUseCase
import com.andyha.weatherdomain.usecase.getCurrentWeather.GetCurrentWeatherUseCase
import com.andyha.weatherdomain.usecase.requestLocationUpdate.RequestLocationUpdateUseCase
import com.andyha.feature.weather.base.BaseWeatherViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class NowViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
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

    private val _currentWeather = MutableStateFlow<List<CurrentWeatherData>>(emptyList())
    val currentWeather = _currentWeather.asStateFlow()

    fun getCurrentWeather() {
        currentLocation?.let { currentLocation ->
            getCurrentWeatherUseCase(Pair(currentLocation.lat, currentLocation.lng))
                .map { it.toCurrentWeatherData() }
                .justLaunch { _currentWeather.emit(it) }
        } ?: requestLocationUpdate()
    }

    override fun onLocationDetected() {
        getCurrentWeather()
    }
}