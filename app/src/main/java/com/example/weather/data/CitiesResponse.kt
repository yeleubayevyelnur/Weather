package com.example.weather.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class CitiesResponse(
    val cities: List<City>
)

@Parcelize
data class City(
    val city: String,
    val weather: String
): Parcelable