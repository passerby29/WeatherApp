package com.passerby.weatherapp.business

import com.passerby.weatherapp.business.model.GeoCodeModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApi {

    @GET("geo/1.0/reverse?")
    fun getCityByCoordinates(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("limit") limit: String = "10",
        @Query("appid") appid: String = "1d58dd30648ea99d0a05250bc7ef799b",
    ): Observable<List<GeoCodeModel>>
}