package com.devtides.countriesapp.di

import com.devtides.countriesapp.model.CountriesApi
import com.devtides.countriesapp.model.CountriesService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {
    private val BASE_URL = "https://raw.githubusercontent.com/"

    @Provides
    fun provideCountriesApi(): CountriesApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            // callAdapterFactory will transform the reposnse into the type which we need in the API service
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CountriesApi::class.java)
    }

    @Provides
    fun provideCountriesService(): CountriesService{
        return CountriesService()
    }


}