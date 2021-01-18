package com.example.hannyweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.hannyweather.logic.Repository
import com.example.hannyweather.logic.model.Place

class PlaceViewModel : ViewModel() {
    // 要查询的数据
    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    val placeLiveData: LiveData<Result<List<Place>>> = Transformations.switchMap(searchLiveData) {
        Repository.searchPlaces(it)
    }

    fun searchPlaces(query: String) {
        // 优化请求
        // 因为一旦设置了liveData的值，就会导致switchMap中的函数调用
        if (searchLiveData.value == null || searchLiveData.value != query) {
            searchLiveData.value = query
        }
    }

    fun savePlace(place: Place) = Repository.savePlace(place)

    fun getSavedPlace() = Repository.getSavedPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()
}