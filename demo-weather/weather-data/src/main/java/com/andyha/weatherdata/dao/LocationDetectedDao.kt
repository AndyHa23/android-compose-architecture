package com.andyha.weatherdata.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andyha.coredata.base.BaseDao
import com.andyha.weatherdata.entity.LocationDetectedEntity
import kotlinx.coroutines.flow.Flow


@Dao
abstract class LocationDetectedDao : BaseDao<LocationDetectedEntity>() {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLocation(obj: LocationDetectedEntity): Long

    @Query("SELECT * FROM ${LocationDetectedEntity.TABLE_NAME} WHERE isSelected=1 LIMIT 1")
    abstract fun getSelectedLocation(): Flow<List<LocationDetectedEntity>>

    @Query("SELECT * FROM ${LocationDetectedEntity.TABLE_NAME} ORDER BY lastUpdated DESC")
    abstract fun getLocations(): Flow<List<LocationDetectedEntity>>

    @Query("UPDATE ${LocationDetectedEntity.TABLE_NAME} SET isSelected=1 WHERE address = :selectedLocation")
    abstract fun setSelected(selectedLocation: String): Int

    @Query("UPDATE ${LocationDetectedEntity.TABLE_NAME} SET isSelected=0 WHERE address <> :selectedLocation")
    abstract fun setUnselected(selectedLocation: String): Int
}
