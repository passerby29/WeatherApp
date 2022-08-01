package com.passerby.weatherapp

import android.app.Application
import android.content.Intent

const val APP_SETTINGS = "App settings"
const val IS_STARTED_UP = "Is started up"

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val prefs = getSharedPreferences(APP_SETTINGS, MODE_PRIVATE)

        val flag = prefs.contains(IS_STARTED_UP)

        if (!flag) {
            val editor = prefs.edit()
            editor.putBoolean(IS_STARTED_UP, true)
            editor.apply()
            val intent = Intent(this, InitialActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}