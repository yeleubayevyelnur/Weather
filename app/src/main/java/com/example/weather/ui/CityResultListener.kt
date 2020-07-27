package com.example.weather.ui

import com.example.weather.data.City

interface CityResultListener {
    fun onCityClickedResult(city: City)
}