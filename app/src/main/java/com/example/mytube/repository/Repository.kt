package com.example.mytube.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.mytube.MainActivity
import com.example.mytube.MyApplication
import com.example.mytube.di.DaggerAppComponent
import com.example.mytube.repository.webservice.WebClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object Repository {

    fun fetchData(end:String, urlResponse: MutableLiveData<VideoResponse>, context: Context){


        WebClient.retrofitClient.getData(end).enqueue(object: Callback<VideoResponse>{
            override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                //if(call.isCanceled || !call.isExecuted)
                urlResponse.postValue(null)
            }



            override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                if (response.isSuccessful && response.body() != null){
                    urlResponse.postValue(response.body()!!)
                }else{
                    urlResponse.postValue(null)
                }
            }
        })
    }
}