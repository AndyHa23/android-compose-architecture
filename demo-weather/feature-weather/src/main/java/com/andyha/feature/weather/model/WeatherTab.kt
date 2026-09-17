package com.andyha.feature.weather.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.andyha.feature.weather.R

enum class WeatherTab(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
) {
    Now("weather-now", R.drawable.ic_live, R.string.now),
    Hourly("weather-hourly", R.drawable.ic_hour, R.string.hourly),
    Daily("weather-daily", R.drawable.ic_daily, R.string.daily),
}
