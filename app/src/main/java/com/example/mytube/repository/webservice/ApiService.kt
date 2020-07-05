package com.example.mytube.repository.webservice

import com.example.mytube.repository.VideoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("{end}")
    fun getData(@Path("end") pageId:String):Call<VideoResponse>
}