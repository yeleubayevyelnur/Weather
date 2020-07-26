package com.example.weather.data

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class CitiesRepository(
    private val apiService: ApiService
) {
    fun fetchCities(city: String?): Single<CitiesResponse> {
        return apiService.getCities(city)
            .subscribeOn(Schedulers.io())
    }
}