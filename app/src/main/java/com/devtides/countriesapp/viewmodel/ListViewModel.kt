package com.devtides.countriesapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devtides.countriesapp.di.DaggerApiComponent
import com.devtides.countriesapp.model.CountriesService
import com.devtides.countriesapp.model.Country
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class ListViewModel : ViewModel() {

    @Inject
    lateinit var countriesService:CountriesService

    init {
        DaggerApiComponent.create().inject(this)
    }

    //private val disposable = CompositeDisposable()
    val countries = MutableLiveData<List<Country>>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()


     fun refresh() {
         viewModelScope.launch(Dispatchers.Main){
             loading.value = true
             fetchListOfCountiesWithCoroutines()
         }
        //fetchCountries()

    }

    /*private fun fetchCountries() {
        loading.value = true
        disposable.add(countriesService.getCountries()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<Country>>() {
                override fun onSuccess(value: List<Country>?) {
                    countries.value = value
                    countryLoadError.value = false
                    loading.value= false
                }

                override fun onError(e: Throwable?) {
                    countryLoadError.value = true
                    loading.value = false
                }
            }))

    }*/

    private suspend fun fetchListOfCountiesWithCoroutines()
    {
        viewModelScope.launch {
            try {
                val result = countriesService.getCountries()
                if(result.isSuccessful)
                {
                    countries.value = result.body()
                    countryLoadError.value = false
                    loading.value= false
                }
                else
                {
                    countryLoadError.value = true
                    loading.value = false
                }
            }
            catch (e:Exception)
            {
                // handle the error
                countryLoadError.value = true
                loading.value = false
            }
        }
    }


    override fun onCleared() {
      //  disposable.clear()
        super.onCleared()
    }


}