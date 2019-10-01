package com.devtides.countriesapp.model
import com.devtides.countriesapp.di.DaggerApiComponent
import dagger.Module
import dagger.Provides
import io.reactivex.Single
import javax.inject.Inject


class CountriesService {

    @Inject
    lateinit var api:CountriesApi

    init {
      DaggerApiComponent.create().inject(this)
    }

    fun getCountries():Single<List<Country>>
    {
        return api.getCountries()
    }

}