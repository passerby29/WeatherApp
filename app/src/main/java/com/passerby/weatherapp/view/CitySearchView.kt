package com.passerby.weatherapp.view

import com.passerby.weatherapp.business.model.GeoCodeModel
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface CitySearchView : MvpView {

    @AddToEndSingle
    fun setLoading(flag: Boolean)

    @AddToEndSingle
    fun fillPredictionsList(data: List<GeoCodeModel>)

    @AddToEndSingle
    fun fillFavoriteList(data: List<GeoCodeModel>)
}