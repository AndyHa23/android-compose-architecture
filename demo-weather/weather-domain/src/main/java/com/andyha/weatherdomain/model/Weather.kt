package com.andyha.weatherdomain.model

data class Weather(
    val date: String, //in format: yyyy/MM/dd
    val address: String,
    val region: String,
    val country: String,
    val lastUpdated: Long, //unix timestamp
    val temperature: Int?, //degree C
    val feelLike: Int?, //degree C
    val isDay: Boolean?, //true=day, false=night
    val icon: String?,
    val condition: String?, //sunny, rainy, cloudy
    val windSpeed: Double?, //km per hour
    val windDegree: Double?,
    val windDirection: String?,
    val pressure: Double?, //in millibars
    val humidity: Int?, //percentage
    val cloudCover: Int?, //percentage
    val visibility: Double?, //in km
    val uv: Double?, //uv index
    val airQuality: Int?,
)
