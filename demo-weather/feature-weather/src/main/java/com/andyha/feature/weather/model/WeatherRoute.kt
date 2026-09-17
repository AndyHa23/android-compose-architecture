package com.andyha.feature.weather.model

sealed class WeatherRoute(val route: String) {
    data object Login : WeatherRoute(LOGIN)
    data object Main : WeatherRoute(MAIN)

    companion object {
        const val LOGIN = "login"
        const val MAIN = "weather-main"
    }
}
