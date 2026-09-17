package com.andyha.weatherdata.networking.response

import com.andyha.weatherdata.networking.request.LocationDto
import com.google.gson.annotations.SerializedName


class WeatherResponse {
    @SerializedName("location")
    var location: LocationDto? = null

    @SerializedName("current")
    var currentWeather: CurrentWeatherDto? = null
}