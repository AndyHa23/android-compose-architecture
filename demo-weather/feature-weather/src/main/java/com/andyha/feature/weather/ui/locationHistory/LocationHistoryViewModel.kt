package com.andyha.feature.weather.ui.locationHistory

import androidx.lifecycle.viewModelScope
import com.andyha.coreextension.TAG
import com.andyha.corenetwork.di.IoDispatcher
import com.andyha.feature.weather.model.Section
import com.andyha.coreui.base.viewModel.BaseViewModel
import com.andyha.coreui.manager.ApiErrorHandler
import com.andyha.weatherdomain.model.LocationState.LocationDetected
import com.andyha.weatherdomain.usecase.getLocationHistory.GetLocationHistoryUseCase
import com.andyha.feature.weather.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class LocationHistoryViewModel @Inject constructor(
    private val getLocationHistoryUseCase: GetLocationHistoryUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val errorHandler: ApiErrorHandler,
) : BaseViewModel(ioDispatcher, errorHandler) {

    private val _locations = MutableStateFlow<List<Section<Int, LocationDetected>>>(listOf())
    val locations = _locations.asStateFlow()

    init {
        viewModelScope.launch {
            getLocationHistoryUseCase().justLaunch {
                Timber.d(TAG, "location history: $it")
                _locations.emit(
                    mutableListOf<Section<Int, LocationDetected>>().apply {
                        if (it.isNotEmpty()) {
                            add(Section(R.string.current_location, listOf(it[0])))
                        }
                        if (it.size > 1) {
                            add(Section(R.string.recent, it.subList(1, it.size)))
                        }
                    }
                )
            }
        }
    }
}