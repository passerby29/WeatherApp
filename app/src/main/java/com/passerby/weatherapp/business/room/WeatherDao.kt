package com.passerby.weatherapp.business.room

import androidx.room.*

@Dao
interface WeatherDao {

    @Query("select * from WeatherData where id = 1")
    fun getWeatherData(): WeatherDataEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherData(data: WeatherDataEntity)

    @Update
    fun updateWeatherData(data: WeatherDataEntity)

    @Delete
    fun deleteWeatherData(data: WeatherDataEntity)
}