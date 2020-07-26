package com.example.weather.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("test_cities/")
    fun getCities(@Query("char") char: String?): Single<CitiesResponse>
}