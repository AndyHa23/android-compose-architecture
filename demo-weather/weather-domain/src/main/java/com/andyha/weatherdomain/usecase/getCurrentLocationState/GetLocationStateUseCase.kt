package com.andyha.weatherdomain.usecase.getCurrentLocationState

import com.andyha.coredata.base.NoInputUseCase
import com.andyha.weatherdomain.model.LocationState
import kotlinx.coroutines.flow.Flow
interface GetLocationStateUseCase: NoInputUseCase<Flow<LocationState>>