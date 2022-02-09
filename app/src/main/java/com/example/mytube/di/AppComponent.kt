package com.example.mytube.di

import android.content.Context
import com.example.mytube.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    //fun providesRetrofitClient
    fun inject(activity: MainActivity)
}