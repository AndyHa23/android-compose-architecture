package com.andyha.weatherdomain.usecase.getLocationHistory

import com.andyha.coredata.base.NoInputUseCase
import com.andyha.weatherdomain.model.LocationState.LocationDetected
import kotlinx.coroutines.flow.Flow


interface GetLocationHistoryUseCase : NoInputUseCase<Flow<List<LocationDetected>>>