package com.andyha.weatherdomain.repository

import com.andyha.weatherdomain.model.LocationState
import com.andyha.weatherdomain.model.LocationState.LocationDetected
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface LocationRepository {
    val currentLocationState: SharedFlow<LocationState>

    fun requestLocationUpdate()
    fun getLocationHistory(): Flow<List<LocationDetected>>
    fun setSelectedLocation(address: String): Flow<Boolean>
}