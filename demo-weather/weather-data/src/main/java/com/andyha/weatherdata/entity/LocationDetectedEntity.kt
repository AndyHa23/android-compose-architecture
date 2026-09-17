package com.andyha.weatherdata.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.andyha.weatherdomain.model.LocationState.LocationDetected

@Entity(
    tableName = LocationDetectedEntity.TABLE_NAME,
    primaryKeys = ["address", "region", "country"],
)
data class LocationDetectedEntity(
    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "region")
    val region: String,

    @ColumnInfo(name = "country")
    val country: String,

    @ColumnInfo(name = "lat")
    val lat: Double,

    @ColumnInfo(name = "lng")
    val lng: Double,

    @ColumnInfo(name = "temperature")
    val temperature: Int? = null,

    @ColumnInfo(name = "icon")
    val icon: String? = null,

    @ColumnInfo(name = "lastUpdated")
    val lastUpdated: Long,

    @ColumnInfo(name = "isSelected")
    val isSelected: Boolean = false,
) {
    companion object {
        const val TABLE_NAME = "LocationDetected"
    }
}

fun LocationDetectedEntity.toDomain() = LocationDetected(
    address = address,
    region = region,
    country = country,
    lat = lat,
    lng = lng,
    temperature = temperature,
    icon = icon,
    lastUpdated = lastUpdated,
    isSelected = isSelected,
)

fun LocationDetected.toEntity() = LocationDetectedEntity(
    address = address,
    region = region,
    country = country,
    lat = lat,
    lng = lng,
    temperature = temperature,
    icon = icon,
    lastUpdated = lastUpdated,
    isSelected = isSelected,
)
