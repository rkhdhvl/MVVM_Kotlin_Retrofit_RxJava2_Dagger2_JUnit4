package com.devtides.countriesapp.model

import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface CountriesApi {
    /*@GET("DevTides/countries/master/countriesV2.json")
    fun getCountries(): Single<List<Country>>*/

    @GET("DevTides/countries/master/countriesV2.json")
    suspend fun getCountries(): Response<List<Country>>
}