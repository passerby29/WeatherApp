package com.passerby.weatherapp.business.model

data class Temp(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
) {
    fun getAverage() = (max + min) / 2
}