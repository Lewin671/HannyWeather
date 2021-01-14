package com.example.hannyweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.hannyweather.logic.Repository
import com.example.hannyweather.logic.model.Place

class PlaceViewModel : ViewModel() {
    // 要查询的数据
    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String) {
        // 优化请求
        // 因为一旦设置了liveData的值，就会导致switchMap中的函数调用
        if (searchLiveData.value == null || searchLiveData.value != query) {
            searchLiveData.value = query
        }
    }
}