package com.andyha.weatherdomain.model

data class HourlyForecast(
    val timeStamp: Long,
    val temperature: Int?,
    val feelsLike: Int?,
    val condition: String?,
    val icon: String?,
    val chanceOfRain: Int?,
    val precipitation: Double?,
)
