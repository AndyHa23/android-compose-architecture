package com.andyha.weatherdomain.model


data class HourlyForecastWithDay(
    val dayTimeStamp: Long,
    val forecast: List<HourlyForecast>
)