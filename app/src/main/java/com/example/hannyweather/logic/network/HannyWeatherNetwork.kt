package com.example.hannyweather.logic.network

import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// 网络数据源入口
object HannyWeatherNetwork {
    private val placeService = ServiceCreator.create<PlaceService>()
    private val weatherService = ServiceCreator.create<WeatherService>()

    suspend fun searchPlace(query: String) = placeService.searchPlaces(query).await()
    suspend fun getDailyWeather(lng: String, lat: String) =
            weatherService.getDailyWeather(lng, lat).await()

    suspend fun getRealTimeWeather(lng: String, lat: String) =
            weatherService.getRealTimeWeather(lng, lat).await()

    // 创建一个挂起协程来获取数据
    private suspend fun <T> Call<T>.await(): T {
        return suspendCancellableCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        // Resumes the execution of the corresponding coroutine passing [body] as the return value of the last suspension point
                        // 将body数据返回到挂起的协程
                        continuation.resume(body)
                    } else {
                        continuation.resumeWithException(
                                RuntimeException("response body is null")
                        )
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }
    }

}