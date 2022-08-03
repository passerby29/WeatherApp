package com.passerby.weatherapp

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.passerby.weatherapp.business.model.*
import com.passerby.weatherapp.presenters.MainPresenter
import com.passerby.weatherapp.view.MainView
import com.passerby.weatherapp.view.adapters.MainDailyListAdapter
import com.passerby.weatherapp.view.adapters.MainHourlyListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

const val TAG = "GEO_TEST"
class MainActivity : MvpAppCompatActivity(), MainView {

    private val mainPresenter by moxyPresenter { MainPresenter() }

    private val geoService by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locationRequest by lazy { initLocationRequest() }
    private lateinit var mLocation: Location

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()

        main_hourly_list.apply {
            adapter = MainHourlyListAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        main_daily_list.apply {
            adapter = MainDailyListAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        mainPresenter.enable()

        geoService.requestLocationUpdates(locationRequest, geoCallBack, null)
    }

    private fun initView() {
        city_textview.text = "Simferopol"
        date_textview.text = "24 july"
        weather_image_add.setImageResource(R.drawable.ic_sun_icon)
        weather_state_tv.text = "sunny"
        weather_image_main.setImageResource(R.mipmap.sun_main1x)
    }

    //-----------------moxy code-----------------
    override fun displayLocation(data: String) {
        city_textview.text = data
    }

    override fun displayCurrentData(data: Current) {
        city_textview.text = "Simferopol"
        date_textview.text = "24 july"
        weather_image_add.setImageResource(R.drawable.ic_sun_icon)
        weather_state_tv.text = "sunny"
        weather_image_main.setImageResource(R.mipmap.sun_main1x)
    }

    override fun displayHourlyData(data: List<HourlyWeatherModel>) {
        (main_hourly_list.adapter as MainHourlyListAdapter).updateData(data)
    }

    override fun displayDailyData(data: List<DailyWeatherModel>) {
        (main_daily_list.adapter as MainDailyListAdapter).updateData(data)
    }

    override fun displayError(error: Throwable) {
    }

    override fun setLoading(flag: Boolean) {
    }
    //-----------------moxy code-----------------

    //-----------------location code-----------------

    private fun initLocationRequest(): LocationRequest {
        val request = LocationRequest.create()
        return request.apply {
            interval = 10000
            fastestInterval = 5000
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
    }

    private val  geoCallBack = object : LocationCallback() {
        override fun onLocationResult(geoRes: LocationResult) {
            Log.d(TAG, "onLocationResult: ${geoRes.locations.size}")
            for (location in geoRes.locations) {
                mLocation = location
                mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
                Log.d(TAG, "onLocationResult: lat: ${location.latitude}; ${location.longitude}")
            }
        }
    }

    //-----------------location code-----------------
}