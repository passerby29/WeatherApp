package com.passerby.weatherapp.business.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WeatherDataEntity::class, GeoCodeEntity::class],
    exportSchema = false,
    version = 4
)
abstract class OpenWeatherDB : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

    abstract fun getGeoCodeDao(): GeoCodeDao

}