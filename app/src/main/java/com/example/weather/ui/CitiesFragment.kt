package com.example.weather.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_cities.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CitiesFragment : Fragment() {
    private val citiesViewModel: CitiesViewModel by viewModel()
    private lateinit var citiesAdapter: CitiesAdapter
    private val disposable = CompositeDisposable()
    private val cities = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cities, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        citiesAdapter = CitiesAdapter(cities)
        rvCities.adapter = citiesAdapter
        rvCities.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        disposable.add(
            citiesViewModel.observeCitiesSubject().subscribe {
                updateCitiesAdapter(it)
            }
        )
        citiesViewModel.fetchCities("А")
    }

    companion object {
        fun newInstance() = CitiesFragment()
    }

    private fun updateCitiesAdapter(data: List<String>) {
        cities.clear()
        cities.addAll(data)
        citiesAdapter.notifyDataSetChanged()
    }
}