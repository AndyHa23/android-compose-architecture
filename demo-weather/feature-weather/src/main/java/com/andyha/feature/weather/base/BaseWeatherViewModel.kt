package com.andyha.feature.weather.base

import androidx.lifecycle.viewModelScope
import com.andyha.corenetwork.di.IoDispatcher
import com.andyha.coreui.base.viewModel.BaseViewModel
import com.andyha.coreui.manager.ApiErrorHandler
import com.andyha.weatherdomain.model.LocationState
import com.andyha.weatherdomain.model.LocationState.LocationDetected
import com.andyha.weatherdomain.usecase.getCurrentLocationState.GetLocationStateUseCase
import com.andyha.weatherdomain.usecase.requestLocationUpdate.RequestLocationUpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
open class BaseWeatherViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val errorHandler: ApiErrorHandler,
    private val getLocationStateUseCase: GetLocationStateUseCase,
    private val requestLocationUpdateUseCase: RequestLocationUpdateUseCase,
) : BaseViewModel(ioDispatcher, errorHandler) {

    private val _currentLocationState = MutableStateFlow<LocationState>(LocationState.Undefined)
    val currentLocationState = _currentLocationState.asStateFlow()

    internal var currentLocation: LocationDetected? = null
    private var locationStateJob: Job? = null

    fun getCurrentLocationState() {
        if (locationStateJob?.isActive == true) return
        locationStateJob = viewModelScope.launch {
            getLocationStateUseCase()
                .catch { Timber.e(it) }
                .collect {
                    _currentLocationState.emit(it)
                    if (it is LocationDetected) {
                        currentLocation = it
                        onLocationDetected()
                    }
                }
        }
    }

    fun requestLocationUpdate() {
        requestLocationUpdateUseCase()
    }

    open fun onLocationDetected() {}
}
