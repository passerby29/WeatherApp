package com.passerby.weatherapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.textview.MaterialTextView
import com.passerby.weatherapp.R
import com.passerby.weatherapp.business.model.DailyWeatherModel
import com.passerby.weatherapp.view.*

class MainDailyListAdapter : BaseAdapter<DailyWeatherModel>() {

    lateinit var clickListener: DayItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_main_daily, parent, false
        )
        return DailyViewHolder(view)
    }

    interface DayItemClickListener {
        fun showDetails(data: DailyWeatherModel)
    }

    @SuppressLint("NonConstantResourceId")
    inner class DailyViewHolder(view: View) : BaseViewHolder(view) {

        @BindView(R.id.day_container)
        lateinit var container: CardView

        @BindView(R.id.item_daily_date_tv)
        lateinit var date: MaterialTextView

        @BindView(R.id.item_daily_pop_tv)
        lateinit var popRate: MaterialTextView

        @BindView(R.id.item_daily_max_temp_tv)
        lateinit var maxTemperature: MaterialTextView

        @BindView(R.id.item_daily_min_temp_tv)
        lateinit var minTemperature: MaterialTextView

        @BindView(R.id.item_daily_weather_icon)
        lateinit var icon: ImageView

        init {
            ButterKnife.bind(this, itemView)
        }

        override fun bindView(position: Int) {
            val itemData = mData[position]

            container.setOnClickListener {
                clickListener.showDetails(itemData)
            }

            if (mData.isNotEmpty()) {
                itemData.apply {
                    val dateOfDay = dt.toDateFormatOf(DAY_WEEK_NAME_LONG)
                    date.text = if (dateOfDay.startsWith("0", true))
                        dateOfDay.removePrefix("0") else dateOfDay

                    if (pop < 0.01) {
                        popRate.visibility = View.INVISIBLE
                    } else {
                        popRate.visibility = View.VISIBLE
                        popRate.text = pop.toPercentString(" %")
                    }
                    icon.setImageResource(weather[0].icon.provideIcon())
                    maxTemperature.text =
                        StringBuilder().append(temp.max.toDegree()).append("\u00b0").toString()
                    minTemperature.text =
                        StringBuilder().append(temp.min.toDegree()).append("\u00b0").toString()
                }
            }
        }
    }
}