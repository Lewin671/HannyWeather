package com.example.hannyweather.logic.model

data class Weather(val realtime: RealTimeResponse.RealTime, val daily: DailyResponse.Daily)