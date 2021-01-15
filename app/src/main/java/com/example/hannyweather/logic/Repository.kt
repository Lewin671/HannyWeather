package com.example.hannyweather.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.example.hannyweather.HannyWeatherApplication
import com.example.hannyweather.logic.dao.PlaceDao
import com.example.hannyweather.logic.model.Place
import com.example.hannyweather.logic.model.Weather
import com.example.hannyweather.logic.network.HannyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {

    //  dao
    fun savePlace(place: Place) {
        PlaceDao.savePlace(place)
    }

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()


    // network
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = HannyWeatherNetwork.searchPlace(query)
        Log.d(HannyWeatherApplication.DEBUG_TAG, "Repository: ${placeResponse.toString()}")
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealTime = async {
                HannyWeatherNetwork.getRealTimeWeather(lng, lat)
            }
            val deferredDaily = async {
                HannyWeatherNetwork.getDailyWeather(lng, lat)
            }

            val realTimeResponse = deferredRealTime.await()
            val dailyResponse = deferredDaily.await()
            if (realTimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather =
                        Weather(realTimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                        RuntimeException(
                                "realtime response status is ${realTimeResponse.status}" +
                                        "daily response status is ${dailyResponse.status}"
                        )
                )
            }
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
            liveData(context) {
                val result = try {
                    block()
                } catch (e: Exception) {
                    Result.failure(e)
                }
                emit(result)
            }
}



