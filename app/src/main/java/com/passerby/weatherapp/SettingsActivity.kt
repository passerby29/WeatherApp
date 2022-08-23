package com.passerby.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButtonToggleGroup
import com.passerby.weatherapp.view.SettingsHolder
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        innerToolbar.setNavigationOnClickListener { onBackPressed() }

        setSavedSettings()

        listOf(groupTemp, groupWindSpeed, groupPressure).forEach {
            it.addOnButtonCheckedListener(
                ToggleButtonClickListener
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SettingsHolder.onDestroy()
    }

    private fun setSavedSettings() {
        groupTemp.check(SettingsHolder.temp.checkedViewId)
        groupWindSpeed.check(SettingsHolder.windSpeed.checkedViewId)
        groupPressure.check(SettingsHolder.pressure.checkedViewId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right)
    }

    private object ToggleButtonClickListener : MaterialButtonToggleGroup.OnButtonCheckedListener {
        override fun onButtonChecked(
            group: MaterialButtonToggleGroup?,
            checkedId: Int,
            isChecked: Boolean
        ) {
            when (checkedId to isChecked) {
                R.id.degreeC to true -> SettingsHolder.temp = SettingsHolder.Setting.TEMP_CELSIUS
                R.id.degreeF to true -> SettingsHolder.temp = SettingsHolder.Setting.TEMP_FAHRENHEIT
                R.id.m_s to true -> SettingsHolder.windSpeed = SettingsHolder.Setting.WIND_SPEED_MS
                R.id.km_h to true -> SettingsHolder.windSpeed =
                    SettingsHolder.Setting.WIND_SPEED_KMH
                R.id.h_pa to true -> SettingsHolder.pressure = SettingsHolder.Setting.PRESSURE_HPA
                R.id.mm_hg to true -> SettingsHolder.pressure = SettingsHolder.Setting.PRESSURE_MMHG
            }
        }
    }
}