package com.example.hannyweather.logic.network

import com.example.hannyweather.HannyWeatherApplication
import com.example.hannyweather.logic.model.PlaceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    @GET("v2/place?&token=${HannyWeatherApplication.TOKEN}&lang=zh_CN")
    suspend fun searchPlaces(@Query("query") query: String): Response<PlaceResponse>
}