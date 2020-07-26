package com.example.weather.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.weather.data.CitiesRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class CitiesViewModel(private val repository: CitiesRepository) : ViewModel() {
    private val disposable = CompositeDisposable()
    private val citiesSubject = PublishSubject.create<List<String>>()

    fun fetchCities(city: String? = null) {
        disposable.add(
            repository.fetchCities(city)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    citiesSubject.onNext(it.cities)
                }, {
                    //todo need to handle exeption
                    Log.d("yel", it.localizedMessage)
                })
        )
    }

    fun observeCitiesSubject(): Observable<List<String>> {
        return citiesSubject
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}