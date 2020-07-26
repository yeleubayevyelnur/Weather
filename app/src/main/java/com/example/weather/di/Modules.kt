package com.example.weather.di

import com.example.weather.data.CitiesRepository
import com.example.weather.data.RetrofitBuilder
import com.example.weather.ui.CitiesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        RetrofitBuilder.api
    }

    single {
        CitiesRepository(get())
    }
}

val viewModelModule = module {
    viewModel {
        CitiesViewModel(repository = get())
    }
}

