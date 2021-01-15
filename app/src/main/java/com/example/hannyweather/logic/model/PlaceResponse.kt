package com.example.hannyweather.logic.model

import com.google.gson.annotations.SerializedName

// 数据模型的定义是根据api的返回响应来定义的
// api样例: responseSample.json

data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(
        val name: String, val location: Location,
        @SerializedName("formatted_address") val address: String
)

// 经度和纬度
data class Location(val lng: String, val lat: String)