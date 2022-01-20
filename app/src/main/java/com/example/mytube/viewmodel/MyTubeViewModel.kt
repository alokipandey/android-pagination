package com.example.mytube.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mytube.repository.webservice.EndPoint
import com.example.mytube.repository.Repository
import com.example.mytube.repository.VideoResponse
import javax.inject.Inject

class MyTubeViewModel @Inject constructor(): ViewModel() {
    val apiResponse = MutableLiveData<VideoResponse>()
    val isLoading = MutableLiveData<Boolean>().apply { value = false }
    var loadedPages = 0
    fun fetchData(end: EndPoint){
        isLoading.postValue(true)
        Repository.fetchData(end.value,apiResponse)
    }
    fun getNextPageEndPoint(): EndPoint {
        when(loadedPages){
            0 -> {return EndPoint.FIRST_PAGE}
            1 -> {return EndPoint.SECOND_PAGE}
            2 -> {return EndPoint.THIRD_PAGE}
            else -> {return EndPoint.BLANK}
        }
    }
}