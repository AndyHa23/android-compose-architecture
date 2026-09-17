package com.andyha.weatherdata.networking.response

import com.google.gson.annotations.SerializedName


class DailyForecastWrapperDto {
    @SerializedName("date_epoch")
    var timeStamp: Long? = null

    @SerializedName("day")
    var day: DailyForecastDto? = null

    @SerializedName("hour")
    var hours: List<HourlyForecastDto> = listOf()
}