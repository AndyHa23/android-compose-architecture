package com.andyha.weathercommon.model

import com.andyha.weatherdomain.model.Weather

/**
 * A single card on the "Now" screen. Every card is an immutable snapshot of the
 * [Weather] it was built from, so emitting a new forecast never mutates a previous one.
 */
sealed interface CurrentWeatherData {

    val weather: Weather

    /** Stable identity for list keys. */
    val key: String

    data class Now(override val weather: Weather) : CurrentWeatherData {
        override val key: String get() = "now"
    }

    data class Metric(
        val type: WeatherMetric,
        override val weather: Weather,
    ) : CurrentWeatherData {
        override val key: String get() = type.name
    }

    data class AirQuality(override val weather: Weather) : CurrentWeatherData {
        override val key: String get() = "air_quality"
    }
}

enum class WeatherMetric {
    Humidity,
    Wind,
    Pressure,
    Cloud,
    Visibility,
    Uv,
}

fun Weather.toCurrentWeatherData(): List<CurrentWeatherData> = buildList {
    add(CurrentWeatherData.Now(this@toCurrentWeatherData))
    WeatherMetric.entries.forEach { metric ->
        add(CurrentWeatherData.Metric(metric, this@toCurrentWeatherData))
    }
    add(CurrentWeatherData.AirQuality(this@toCurrentWeatherData))
}
