package com.example.weather.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.BuildConfig
import com.example.weather.R
import com.example.weather.data.City
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_cities.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class CitiesFragment : Fragment(), CityResultListener {
    private val citiesViewModel: CitiesViewModel by viewModel()
    private lateinit var citiesAdapter: CitiesAdapter
    private val disposable = CompositeDisposable()
    private val cities = ArrayList<City>()
    private val DELAY_TIME: Long = 3000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cities, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        citiesAdapter = CitiesAdapter(cities)
        citiesAdapter.setCityListener(this)
        rvCities.adapter = citiesAdapter
        rvCities.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        disposable.add(
            citiesViewModel.observeCitiesSubject().subscribe {
                updateCitiesAdapter(it)
            }
        )

        disposable.add(
            citiesViewModel.observeErrorsSubject().subscribe {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        )

        val etCityNameObservable = Observable.create(ObservableOnSubscribe<String> { subscriber ->
            etCityName?.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    subscriber.onNext(s.toString())
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {
                }
            })
        })

        disposable.add(etCityNameObservable.map { it.trim() }
            .debounce(DELAY_TIME, TimeUnit.MILLISECONDS)
            .subscribe { text ->
                citiesViewModel.fetchCities(text)
                Log.d(BuildConfig.LOG_TAG_D, text)
            })

        citiesViewModel.fetchCities()
    }

    companion object {
        fun newInstance() = CitiesFragment()
    }

    private fun updateCitiesAdapter(data: List<City>) {
        cities.clear()
        cities.addAll(data)
        citiesAdapter.notifyDataSetChanged()
    }

    override fun onCityClickedResult(city: City) {
        openWeatherByCity(WeatherFragment.newInstance(city))
    }

    private fun openWeatherByCity(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, fragment)
            ?.addToBackStack(fragment::class.java.name)
            ?.commit()
    }
}