package com.example.hannyweather.logic.network

import com.example.hannyweather.HannyWeatherApplication
import com.example.hannyweather.logic.model.DailyResponse
import com.example.hannyweather.logic.model.RealTimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    // lng和lat分别为经度和纬度
    @GET("v2.5/${HannyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealTimeWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<RealTimeResponse>

    @GET("/v2.5/${HannyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>
}