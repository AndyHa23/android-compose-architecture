package com.andyha.feature.weather.compose

import android.content.Context
import com.andyha.coreextension.getDrawableResourceByName
import com.andyha.coreextension.getStringResourceByName
import com.andyha.coreutils.timeFormat.TimeFormatter
import com.andyha.feature.weather.R
import java.util.Calendar
import java.util.Date

fun Context.formatLastUpdated(timestamp: Long): String {
    return getString(
        R.string.last_updated,
        TimeFormatter.timestampToHourMinute(this, timestamp).orEmpty()
    )
}

fun Context.formatWeekDay(timestamp: Long): String =
    TimeFormatter.timestampToWeekDay(this, timestamp).orEmpty()

fun Context.formatHour(timestamp: Long): String =
    TimeFormatter.timestampToHourMinute(this, timestamp).orEmpty()

fun Context.formatDate(timestamp: Long): String =
    TimeFormatter.timestampToDate(this, timestamp).orEmpty()

fun formatShortWeekDay(timestamp: Long): String =
    TimeFormatter.getDateFormat("EEE").format(Date(timestamp))

fun isToday(timestamp: Long): Boolean {
    val today = Calendar.getInstance()
    val target = Calendar.getInstance().apply { timeInMillis = timestamp }
    return today.get(Calendar.YEAR) == target.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == target.get(Calendar.DAY_OF_YEAR)
}

fun Context.airQualityDrawable(quality: Int?): Int? {
    quality ?: return null
    return getDrawableResourceByName("ic_aqi_$quality")
}

fun Context.airQualityDescription(quality: Int?): String {
    return quality?.let { value ->
        getStringResourceByName("air_quality_$value")?.let(::getString)
    } ?: getString(R.string.air_quality_na)
}
