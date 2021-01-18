package com.example.hannyweather.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.hannyweather.Logger
import com.example.hannyweather.logic.dao.PlaceDao
import com.example.hannyweather.logic.model.Place
import com.example.hannyweather.logic.model.Weather
import com.example.hannyweather.logic.network.HannyWeatherNetwork
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

object Repository {

    //  dao
    fun savePlace(place: Place) {
        PlaceDao.savePlace(place)
    }

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

    private val coroutineContext: CoroutineContext = CoroutineScope(Dispatchers.IO).coroutineContext

    // network
    fun searchPlaces(query: String): LiveData<Result<List<Place>>> {
        // 取消之前的请求
        Logger.d("搜索内容为 $query")
        coroutineContext.cancelChildren()

        return fire {
            val placeResponse = HannyWeatherNetwork.searchPlace(query)
            // 模拟网络延迟
            Logger.d("搜索内容为 $query, 响应为${placeResponse.toString()}")
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }
    }

    fun refreshWeather(lng: String, lat: String): LiveData<Result<Weather>> {
        coroutineContext.cancelChildren()
        return fire {
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
    }


    private fun <T> fire(block: suspend () -> Result<T>) =
            liveData(coroutineContext) {
                val result = try {
                    block()
                } catch (e: Exception) {
                    Result.failure(e)
                }
                emit(result)
            }
}



