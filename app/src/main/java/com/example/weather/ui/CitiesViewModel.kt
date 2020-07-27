package com.example.weather.ui

import androidx.lifecycle.ViewModel
import com.example.weather.data.CitiesRepository
import com.example.weather.data.City
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class CitiesViewModel(private val repository: CitiesRepository) : ViewModel() {
    private val disposable = CompositeDisposable()
    private val citiesSubject = PublishSubject.create<List<City>>()
    private val errorsSubject = PublishSubject.create<String>()

    fun fetchCities(city: String? = null) {
        disposable.add(
            repository.fetchCities(city)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    citiesSubject.onNext(it.cities)
                }, {
                    it?.message?.let { msg ->
                        errorsSubject.onNext(msg)
                    }
                })
        )
    }

    fun observeCitiesSubject(): Observable<List<City>> {
        return citiesSubject
    }

    fun observeErrorsSubject(): Observable<String> {
        return errorsSubject
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }
}