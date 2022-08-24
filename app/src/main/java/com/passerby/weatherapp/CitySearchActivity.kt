package com.passerby.weatherapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.passerby.weatherapp.business.model.GeoCodeModel
import com.passerby.weatherapp.presenters.CitySearchPresenter
import com.passerby.weatherapp.view.CitySearchView
import com.passerby.weatherapp.view.adapters.CityListAdapter
import com.passerby.weatherapp.view.createObservable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_city_search.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.util.concurrent.TimeUnit

class CitySearchActivity : MvpAppCompatActivity(), CitySearchView {

    private val presenter by moxyPresenter { CitySearchPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_search)

        presenter.enable()
        presenter.getFavoriteList()

        initCityList(predictions)
        initCityList(favorites)

        search_field.createObservable()
            .doOnNext { setLoading(true) }
            .debounce(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (!it.isNullOrEmpty()) presenter.searchFor(it)
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_left)
    }
    //-------------------------------- ^ CitySearchActivity ^ --------------------------------


    //-------------------------------- moxy --------------------------------
    override fun setLoading(flag: Boolean) {
        loading.isActivated = flag
        loading.visibility = if (flag) View.VISIBLE else View.GONE
    }

    override fun fillPredictionsList(data: List<GeoCodeModel>) {
        (predictions.adapter as CityListAdapter).updateData(data)
    }

    override fun fillFavoriteList(data: List<GeoCodeModel>) {
        (favorites.adapter as CityListAdapter).updateData(data)
    }
    //-------------------------------- moxy --------------------------------

    private fun initCityList(rv: RecyclerView) {
        val cityAdapter = CityListAdapter()
        cityAdapter.clickListener = searchItemClickListener
        rv.apply {
            adapter = cityAdapter
            layoutManager = object : LinearLayoutManager(
                this@CitySearchActivity, VERTICAL, false
            ) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            setHasFixedSize(true)
        }
    }

    private val searchItemClickListener = object : CityListAdapter.SearchItemClickListener {
        override fun removeFromFavorite(item: GeoCodeModel) {
            presenter.removeLocation(item)
        }

        override fun addToFavorite(item: GeoCodeModel) {
            presenter.saveLocation(item)
        }

        override fun showWeatherIn(item: GeoCodeModel) {
            val intent = Intent(this@CitySearchActivity, MainActivity::class.java)
            val bundle = Bundle()
            bundle.putString("lat",item.lat.toString())
            bundle.putString("lon",item.lon.toString())
            intent.putExtra(COORDINATES,bundle)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_left)
        }
    }
}