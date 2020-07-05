package com.example.mytube.repository.webservice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WebClient {
    private val BASE_URL = "http://www.mocky.io/v2/"
    val retrofitClient by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiService::class.java)
        }
}

enum class EndPoint(val value:String){
    FIRST_PAGE("59b3f0b0100000e30b236b7e"),
    SECOND_PAGE("59ac28a9100000ce0bf9c236"),
    THIRD_PAGE("59ac293b100000d60bf9c239"),
    BLANK("")
}