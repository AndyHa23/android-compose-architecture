package com.andyha.weatherdomain.model

sealed class LocationState {
    object Undefined : LocationState()
    object PermissionDenied : LocationState()
    object PermissionDeniedForever : LocationState()
    object LocationGetFailed : LocationState()

    /**
     * Device location settings are off. [exception] is platform specific (on Android it is a
     * resolvable Play Services exception) and is intentionally kept as a plain [Throwable]
     * so the domain layer stays free of platform dependencies.
     */
    data class LocationSettingOff(val exception: Throwable) : LocationState()

    data class LocationDetected(
        val address: String,
        val region: String,
        val country: String,
        val lat: Double,
        val lng: Double,
        val temperature: Int? = null,
        val icon: String? = null,
        val lastUpdated: Long,
        val isSelected: Boolean = false,
    ) : LocationState()
}
