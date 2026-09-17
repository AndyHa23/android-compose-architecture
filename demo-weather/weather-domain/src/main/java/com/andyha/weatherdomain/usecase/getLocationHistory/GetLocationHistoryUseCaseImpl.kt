package com.andyha.weatherdomain.usecase.getLocationHistory

import com.andyha.weatherdomain.model.LocationState.LocationDetected
import com.andyha.weatherdomain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetLocationHistoryUseCaseImpl @Inject constructor(
    private val locationRepository: LocationRepository
): GetLocationHistoryUseCase {
    override fun invoke(): Flow<List<LocationDetected>> {
        return locationRepository.getLocationHistory()
    }
}