package com.devtides.countriesapp.di

import com.devtides.countriesapp.model.CountriesService
import com.devtides.countriesapp.viewmodel.ListViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(service:CountriesService)
    fun inject(intoViewModel: ListViewModel)
}