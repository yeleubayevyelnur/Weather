package com.example.weather.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.data.City

class CitiesAdapter(private val cities: List<City>) :
    RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {

    private lateinit var cityClickListener: CityResultListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.city.text = cities[position].city
        holder.city.setOnClickListener {
            cityClickListener.onCityClickedResult(cities[position])
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val city: TextView = itemView.findViewById(R.id.city)
    }

    fun setCityListener(listener: CityResultListener){
        cityClickListener = listener
    }
}