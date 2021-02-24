package com.example.hannyweather.logic.network

import com.example.hannyweather.HannyWeatherApplication
import com.example.hannyweather.logic.model.DailyResponse
import com.example.hannyweather.logic.model.RealTimeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    // lng和lat分别为经度和纬度
    @GET("v2.5/${HannyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    suspend fun getRealTimeWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Response<RealTimeResponse>

    @GET("/v2.5/${HannyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    suspend fun getDailyWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Response<DailyResponse>
}