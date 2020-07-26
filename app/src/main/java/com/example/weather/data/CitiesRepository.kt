package com.example.weather.data

import android.content.SharedPreferences
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class CitiesRepository(
    private val apiService: ApiService,
    private val sPrefs: SharedPreferences,
    private val gson: Gson
) {
    private val CITIES = "cities"

    fun fetchCities(city: String?): Single<CitiesResponse> {
        if (sPrefs.getString(CITIES, null) != null && city.isNullOrEmpty()){
            return Single.just(gson.fromJson(sPrefs.getString(CITIES, null), CitiesResponse::class.java))
        }
        return apiService.getCities(city)
            .subscribeOn(Schedulers.io())
            .map {
                sPrefs.edit().putString(CITIES, gson.toJson(it)).apply()
                it
            }
    }
}