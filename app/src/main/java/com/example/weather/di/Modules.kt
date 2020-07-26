package com.example.weather.di

import androidx.preference.PreferenceManager
import com.example.weather.BuildConfig
import com.example.weather.data.ApiService
import com.example.weather.data.CitiesRepository
import com.example.weather.ui.CitiesViewModel
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        CitiesRepository(get(), get(), get())
    }

    single {
        PreferenceManager.getDefaultSharedPreferences(androidContext())
    }
}

val networkModule = module {
    single {
        Gson()
    }

    single {
        OkHttpClient.Builder()
            .followRedirects(false)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    single {
        get<Retrofit>(Retrofit::class.java).create(ApiService::class.java)
    }
}

val viewModelModule = module {
    viewModel {
        CitiesViewModel(repository = get())
    }
}

