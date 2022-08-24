package com.passerby.weatherapp.business.room

import androidx.room.*

@Dao
interface GeoCodeDao {

    @Query("select * from GeoCode")
    fun getAll(): List<GeoCodeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(item: GeoCodeEntity)

    @Delete
    fun remove(item: GeoCodeEntity)
}