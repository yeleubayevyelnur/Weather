package com.example.weather.ui

import android.app.Application
import com.example.weather.di.appModule
import com.example.weather.di.networkModule
import com.example.weather.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApplication)
            androidLogger()
            modules(listOf(
                viewModelModule,
                appModule,
                networkModule
            ))
        }
    }
}