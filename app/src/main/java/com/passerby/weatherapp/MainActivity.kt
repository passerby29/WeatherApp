package com.passerby.weatherapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.passerby.weatherapp.business.model.DailyWeatherModel
import com.passerby.weatherapp.business.model.HourlyWeatherModel
import com.passerby.weatherapp.business.model.WeatherDataModel
import com.passerby.weatherapp.presenters.MainPresenter
import com.passerby.weatherapp.view.*
import com.passerby.weatherapp.view.adapters.MainHourlyListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import kotlin.math.roundToInt

const val TAG = "GEO_TEST"
const val COORDINATES = "Coordinates"

class MainActivity : MvpAppCompatActivity(), MainView {

    private val mainPresenter by moxyPresenter { MainPresenter() }

    private val geoService by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locationRequest by lazy {
        LocationRequest.create().apply {
            interval = 100000
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
    private lateinit var mLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initBottomSheets()
        initSwipeRefresh()

        refresh.isRefreshing = true

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, DailyListFragment(), DailyListFragment::class.simpleName)
            .commit()

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
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()

        if (!intent.hasExtra(COORDINATES)) {
            geoService.requestLocationUpdates(locationRequest, geoCallBack, null)
        } else {

            val coord = intent.extras!!.getBundle(COORDINATES)!!
            val loc = Location("")
            loc.latitude = coord.getString("lat")!!.toDouble()
            loc.longitude = coord.getString("lon")!!.toDouble()
            mLocation = loc
            mainPresenter.refresh(
                lat = mLocation.latitude.toString(),
                lon = mLocation.longitude.toString()
            )
        }

        main_hourly_list.apply {
            adapter = MainHourlyListAdapter()
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL,
                false
            )
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

    private fun initBottomSheets() {
        main_bottom_sheet.isNestedScrollingEnabled = true
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        bottom_sheet_container.layoutParams =
            CoordinatorLayout.LayoutParams(size.x, (size.y * 0.55).roundToInt())
    }

    @SuppressLint("ResourceAsColor")
    private fun initSwipeRefresh() {
        refresh.apply {
            setColorSchemeColors(R.color.purple_700)
            setProgressViewEndTarget(false, 250)
            setOnRefreshListener {
                mainPresenter.refresh(
                    mLocation.latitude.toString(),
                    mLocation.longitude.toString()
                )
            }
        }
    }

    //-----------------moxy code-----------------
    override fun displayLocation(data: String) {
        city_textview.text = data
    }

    @SuppressLint("ResourceType")
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
            val pressureSet = SettingsHolder.pressure
            main_pressure_mu_tv.text = getString(
                pressureSet.measureUnitStringRes,
                pressureSet.getValue(current.pressure.toDouble())
            )
            main_humidity_tv.text =
                StringBuilder().append(current.humidity.toString()).append(" %").toString()
            val windSpeedSet = SettingsHolder.windSpeed
            main_wind_speed_mu_tv.text = getString(
                windSpeedSet.measureUnitStringRes, windSpeedSet.getValue(current.wind_speed)
            )
            main_sunrise_tv.text = current.sunrise.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
            main_sunset_tv.text = current.sunset.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
        }
    }

    override fun displayHourlyData(data: List<HourlyWeatherModel>) {
        (main_hourly_list.adapter as MainHourlyListAdapter).updateData(data)
    }

    override fun displayDailyData(data: List<DailyWeatherModel>) {
        (supportFragmentManager.findFragmentByTag(DailyListFragment::class.simpleName) as
                DailyListFragment).setData(data)
    }

    override fun displayError(error: Throwable) {
    }

    override fun setLoading(flag: Boolean) {
        refresh.isRefreshing = flag
    }
}
//-----------------moxy code-----------------