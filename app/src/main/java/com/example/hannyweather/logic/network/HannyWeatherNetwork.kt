package com.example.hannyweather.logic.network

import retrofit2.Response

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
    private fun <T> Response<T>.await(): T = this.body() ?: throw Exception("the response is null")

}