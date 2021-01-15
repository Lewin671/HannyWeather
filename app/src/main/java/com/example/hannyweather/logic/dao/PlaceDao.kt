package com.example.hannyweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.example.hannyweather.HannyWeatherApplication
import com.example.hannyweather.logic.model.Place
import com.google.gson.Gson
import kotlin.concurrent.thread

object PlaceDao {
    fun savePlace(place: Place) {
        sharePreferences().edit().apply {
            putString("place", Gson().toJson(place))
            apply()
        }
    }

    fun getSavedPlace(): Place {
        val placeJson = sharePreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = sharePreferences().contains("place")

    private fun sharePreferences() = HannyWeatherApplication
            .context.getSharedPreferences("hanny_weather", Context.MODE_PRIVATE)
}