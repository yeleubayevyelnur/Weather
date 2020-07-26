package com.example.weather.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.BuildConfig
import com.example.weather.R
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_cities.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class CitiesFragment : Fragment() {
    private val citiesViewModel: CitiesViewModel by viewModel()
    private lateinit var citiesAdapter: CitiesAdapter
    private val disposable = CompositeDisposable()
    private val cities = ArrayList<String>()
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
        rvCities.adapter = citiesAdapter
        rvCities.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        disposable.add(
            citiesViewModel.observeCitiesSubject().subscribe {
                updateCitiesAdapter(it)
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

    private fun updateCitiesAdapter(data: List<String>) {
        cities.clear()
        cities.addAll(data)
        citiesAdapter.notifyDataSetChanged()
    }
}