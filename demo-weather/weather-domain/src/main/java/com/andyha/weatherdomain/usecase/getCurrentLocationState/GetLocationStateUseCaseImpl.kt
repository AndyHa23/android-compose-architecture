package com.andyha.weatherdomain.usecase.getCurrentLocationState

import com.andyha.weatherdomain.model.LocationState
import com.andyha.weatherdomain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetLocationStateUseCaseImpl @Inject constructor(
    private val locationRepository: LocationRepository
): GetLocationStateUseCase {
    override fun invoke(): Flow<LocationState> {
        return locationRepository.currentLocationState
    }
}