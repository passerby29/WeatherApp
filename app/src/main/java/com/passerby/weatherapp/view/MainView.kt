package com.passerby.weatherapp.view

import com.passerby.weatherapp.business.model.Current
import com.passerby.weatherapp.business.model.DailyWeatherModel
import com.passerby.weatherapp.business.model.HourlyWeatherModel
import com.passerby.weatherapp.business.model.Weather
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MainView : MvpView {

    @AddToEndSingle
    fun displayLocation(data: String)

    @AddToEndSingle
    fun displayCurrentData(data: Current)

    @AddToEndSingle
    fun displayHourlyData(data: List<HourlyWeatherModel>)

    @AddToEndSingle
    fun displayDailyData(data: List<DailyWeatherModel>)

    @AddToEndSingle
    fun displayError(error: Throwable)

    @AddToEndSingle
    fun setLoading(flag: Boolean)
}