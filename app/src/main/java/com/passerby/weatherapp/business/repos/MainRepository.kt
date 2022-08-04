package com.passerby.weatherapp.business.repos

import android.util.Log
import com.passerby.weatherapp.business.ApiProvider
import com.passerby.weatherapp.business.model.WeatherDataModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

const val TAG = "MAIN_REPO"

class MainRepository(api: ApiProvider) : BaseRepository<MainRepository.ServerResponse>(api) {
    fun reloadData(lat: String, lon: String) {
        Observable.zip(
            api.provideWeatherApi().getWeatherForecast(lat, lon),
            api.provideGeoCodeApi().getCityByCoordinates(lat, lon).map {
                it.asSequence().map { model -> model.name }
                    .toList()
                    .filterNotNull()
                    .first()
            },
            { weatherData, geoCode -> ServerResponse(geoCode, weatherData) }
        )
            .subscribeOn(Schedulers.io())
            .doOnNext { }
            //.onErrorResumeNext {  }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                dataEmitter.onNext(it)
            }, {
                Log.d(TAG, "reloadData: $it")
            })
    }

    data class ServerResponse(
        val cityName: String,
        val weatherData: WeatherDataModel,
        val error: Throwable? = null
    )

}