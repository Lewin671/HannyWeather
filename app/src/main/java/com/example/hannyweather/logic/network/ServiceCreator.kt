package com.example.hannyweather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 用于创建Retrofit代理对象
object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com/"

    // retrofit对象
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 两种创建Retrofit代理对象的方法
    // val appService = ServiceCreator.create(AppService::class.java)
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    // val appService = ServiceCreator.create<AppService>()
    inline fun <reified T> create(): T = create(T::class.java)
}