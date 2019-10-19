package com.devtides.countriesapp.model
import com.devtides.countriesapp.di.DaggerApiComponent
import dagger.Module
import dagger.Provides
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.Response
import javax.inject.Inject


class CountriesService {

    // Injecting an instance of CountriesApi into the CountriesService class
    @Inject
    lateinit var api:CountriesApi

    init {
      DaggerApiComponent.create().inject(this)
    }

    /*fun getCountries():Single<List<Country>>
    {
        return api.getCountries()
    }*/

    suspend fun getCountries():Response<List<Country>>
    {
        return api.getCountries()
    }

}