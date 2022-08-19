package com.passerby.weatherapp.business

import com.passerby.weatherapp.business.model.GeoCodeModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApi {

    @GET("geo/1.0/direct?")
    fun getCityByName(
        @Query("q") name: String,
        @Query("appid") appid: String = "6af350ea0bb6c5206720a8ab99bc38f6",
    ): Observable<List<GeoCodeModel>>

    @GET("geo/1.0/reverse?")
    fun getCityByCoordinates(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") id: String = "0399d9350cf75f90f3d93037f1515e67",
    ): Observable<List<GeoCodeModel>>
}