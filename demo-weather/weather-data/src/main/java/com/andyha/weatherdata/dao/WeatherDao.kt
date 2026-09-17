package com.andyha.weatherdata.dao

import androidx.room.Dao
import androidx.room.Query
import com.andyha.coredata.base.BaseDao
import com.andyha.weatherdata.entity.WeatherEntity


@Dao
abstract class WeatherDao: BaseDao<WeatherEntity>(){
    @Query("SELECT * FROM ${WeatherEntity.TABLE_NAME} ORDER BY date DESC LIMIT 1")
    abstract fun getCurrentWeather(): List<WeatherEntity>
}