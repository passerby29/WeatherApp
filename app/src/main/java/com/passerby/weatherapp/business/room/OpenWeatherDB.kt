package com.passerby.weatherapp.business.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherDataEntity::class], exportSchema = false, version = 1)
abstract class OpenWeatherDB : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

}