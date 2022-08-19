package com.passerby.weatherapp.presenters

import android.util.Log
import com.passerby.weatherapp.business.ApiProvider
import com.passerby.weatherapp.business.model.GeoCodeModel
import com.passerby.weatherapp.business.repos.CitySearchRepository
import com.passerby.weatherapp.business.repos.SAVED
import com.passerby.weatherapp.view.CitySearchView

class CitySearchPresenter : BasePresenter<CitySearchView>() {
    private val repo = CitySearchRepository(ApiProvider())

    override fun enable() {
        repo.dataEmitter.subscribe {
            viewState.setLoading(false)
            if (it.purpose == SAVED) {
                Log.d("123123", "enable: SAVED ${it.data}")
                viewState.fillFavoriteList(it.data)
            } else {
                Log.d("123123", "enable: CURRENT ${it.data}")
                viewState.fillPredictionsList(it.data)
            }
        }
    }

    fun searchFor(str: String) {
        repo.getCities(str)
    }

    fun removeLocation(data: GeoCodeModel) {
        repo.remove(data)
    }

    fun saveLocation(data: GeoCodeModel) {
        repo.add(data)
    }

    fun getFavoriteList() {
        repo.updateFavorite()
    }
}