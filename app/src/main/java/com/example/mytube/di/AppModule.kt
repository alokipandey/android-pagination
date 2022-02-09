package com.example.mytube.di

import com.example.mytube.repository.webservice.ApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {
    private val BASEURL = "http://www.mocky.io/v2/"

    @Singleton
    @Provides
    fun providesRetrofitClient(): ApiService{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASEURL)
            .build()
            .create(ApiService::class.java)
    }
}