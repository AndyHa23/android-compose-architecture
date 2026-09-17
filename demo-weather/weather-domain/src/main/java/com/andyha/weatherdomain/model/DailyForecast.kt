package com.andyha.weatherdomain.model

data class DailyForecast(
    val timeStamp: Long,
    val maxTemp: Int?,
    val minTemp: Int?,
    val precipitation: Double?,
    val chanceOfRain: Int?,
    val condition: String?,
    val icon: String?,
)
