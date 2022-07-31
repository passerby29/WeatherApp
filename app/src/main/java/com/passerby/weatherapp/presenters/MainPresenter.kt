package com.passerby.weatherapp.presenters

import com.passerby.weatherapp.view.MainView
import moxy.MvpView

class MainPresenter : BasePresenter<MainView>() {
    override fun enable() {

    }

    fun refresh(lat: String, lon:String){
        viewState.setLoading(true)
    }
}