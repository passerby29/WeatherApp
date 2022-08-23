package com.passerby.weatherapp

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.passerby.weatherapp.business.model.*
import com.passerby.weatherapp.presenters.CitySearchPresenter
import com.passerby.weatherapp.presenters.MainPresenter
import com.passerby.weatherapp.view.*
import com.passerby.weatherapp.view.adapters.MainDailyListAdapter
import com.passerby.weatherapp.view.adapters.MainHourlyListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.lang.StringBuilder

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

        if (!intent.hasExtra(COORDINATE)) {
            geoService.requestLocationUpdates(locationRequest, geoCallBack, null)
        } else {

            val coord = intent.extras!!.getBundle(COORDINATE)!!
            val loc = Location("")
            loc.latitude = coord.getString("lat")!!.toDouble()
            loc.longitude = coord.getString("lon")!!.toDouble()
            mLocation = loc
            mainPresenter.refresh(
                lat = mLocation.latitude.toString(),
                lon = mLocation.longitude.toString()
            )
        }

        menu_btn_main.setOnClickListener {
            val intent = Intent(this, CitySearchActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, android.R.anim.fade_out)
        }

        settings_btn_main.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out)
        }

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

    override fun displayCurrentData(data: WeatherDataModel) {
        data.apply {
            date_textview.text = current.dt.toDateFormatOf(DAY_FULL_MONTH_NAME)
            weather_image_add.setImageResource(current.weather[0].icon.provideIcon())
            main_temp.text =
                StringBuilder().append(current.temp.toDegree()).append("\u00b0").toString()
            daily[0].temp.apply {
                main_temp_min_tv.text =
                    StringBuilder().append(min.toDegree()).append("\u00b0").toString()
                main_temp_max_tv.text =
                    StringBuilder().append(max.toDegree()).append("\u00b0").toString()
            }
            weather_state_tv.text = current.weather[0].description
            weather_image_main.setImageResource(current.weather[0].icon.provideMainIcon())
            main_pressure_tv.text =
                StringBuilder().append(current.pressure.toString()).append(" hPa").toString()
            main_humidity_tv.text =
                StringBuilder().append(current.humidity.toString()).append(" %").toString()
            main_wind_speed_tv.text =
                StringBuilder().append(current.wind_speed.toString()).append(" m/s").toString()
            main_sunrise_tv.text = current.sunrise.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
            main_sunset_tv.text = current.sunset.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
        }
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

    private val geoCallBack = object : LocationCallback() {
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