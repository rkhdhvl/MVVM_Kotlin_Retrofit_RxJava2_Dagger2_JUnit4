package com.devtides.countriesapp.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.devtides.countriesapp.R
import com.devtides.countriesapp.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // lateinit indicates that the property will be initialzed at a later point
    lateinit var viewModel: ListViewModel
    private val countryListAdapter = CountryListAdapter(arrayListOf())
    private var myJob:Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // instantiating the viewModel
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        executeCoroutineToFecthData()
        //viewModel.refresh()

        countriesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countryListAdapter
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing=false
            executeCoroutineToFecthData()
          //  viewModel.refresh()
        }

        observeViewModel()
    }

    private fun executeCoroutineToFecthData()
    {
        myJob = CoroutineScope(Dispatchers.IO).launch {
            viewModel.refresh()
        }
    }

    fun observeViewModel() {

        viewModel.countries.observe(this, Observer { countries ->
            countries?.let {
                countriesList.visibility=View.VISIBLE
                countryListAdapter.updateCountries(countries)
            }
        })

        viewModel.countryLoadError.observe(this, Observer { isError -> isError?.let {
            list_error.visibility = if (it) View.VISIBLE else View.GONE
        }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
          isLoading?.let { loading_view.visibility = if (it) View.VISIBLE else View.GONE
          if(it) {
              list_error.visibility = View.GONE
              countriesList.visibility = View.GONE
          }}
         })
    }

    override fun onDestroy() {
        myJob?.cancel()
        super.onDestroy()
    }
}
