package com.passerby.weatherapp.presenters

import android.util.Log
import com.passerby.weatherapp.business.ApiProvider
import com.passerby.weatherapp.business.repos.MainRepository
import com.passerby.weatherapp.view.MainView

class MainPresenter : BasePresenter<MainView>() {
    private val repo = MainRepository(ApiProvider())

    override fun enable() {
        repo.dataEmitter.subscribe { response ->
            Log.d("MAIN_REPO", "Presenter enable(): $response")
            viewState.displayLocation(response.cityName)
            viewState.displayCurrentData(response.weatherData)
            viewState.displayDailyData(response.weatherData.daily)
            viewState.displayHourlyData(response.weatherData.hourly)
            response.error?.let { viewState.displayError(response.error) }
        }
    }

    fun refresh(lat: String, lon: String) {
        viewState.setLoading(true)
        repo.reloadData(lat, lon)
    }
}