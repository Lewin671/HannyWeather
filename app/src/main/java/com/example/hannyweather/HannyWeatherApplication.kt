package com.example.hannyweather

import android.app.Application
import android.content.Context

class HannyWeatherApplication : Application() {
    companion object {
        lateinit var context: Context
        //const val TOKEN = "F6FpeVaXQXs9T3A9"
        const val TOKEN = "xtioT0MYyJ9BLFeh"
        const val DEBUG_TAG = "HannyWeatherDebugTag"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}