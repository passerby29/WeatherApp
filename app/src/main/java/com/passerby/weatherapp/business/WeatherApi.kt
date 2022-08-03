package com.passerby.weatherapp.business

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/onecall?")
    fun getWeatherForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = "minutely, alerts",
        @Query("appid") appid: String = "1d58dd30648ea99d0a05250bc7ef799b",
        @Query("lang") lang: String = "en",
    )
}