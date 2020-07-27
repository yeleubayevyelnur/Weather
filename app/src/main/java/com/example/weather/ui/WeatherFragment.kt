package com.example.weather.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.data.City
import kotlinx.android.synthetic.main.fragment_weather.*

class WeatherFragment : Fragment() {
    private val CITY_CONST: String = "city"
    private var city: City? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        city = arguments?.getParcelable(CITY_CONST)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        cityName.text = city?.city
        context?.let {
            Glide.with(it)
                .load(Uri.parse(city?.weather))
                .into(ivWeather)
        }
    }

    companion object {
        fun newInstance(city: City) = WeatherFragment().apply {
            arguments = Bundle().apply {
                putParcelable(CITY_CONST, city)
            }
        }
    }
}