package com.andyha.weatherdomain.usecase.requestLocationUpdate

import com.andyha.weatherdomain.repository.LocationRepository
import javax.inject.Inject


class RequestLocationUpdateUseCaseImpl @Inject constructor(
    private val locationRepository: LocationRepository
): RequestLocationUpdateUseCase {

    override fun invoke(){
        return locationRepository.requestLocationUpdate()
    }
}