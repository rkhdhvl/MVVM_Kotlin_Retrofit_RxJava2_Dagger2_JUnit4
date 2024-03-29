package com.devtides.countriesapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devtides.countriesapp.R
import com.devtides.countriesapp.model.Country
import com.devtides.countriesapp.util.getProgressDrawable
import com.devtides.countriesapp.util.loadImage
import kotlinx.android.synthetic.main.item_country.view.*

class CountryListAdapter(var countries:ArrayList<Country>):RecyclerView.Adapter<CountryListAdapter.CountryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CountryViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_country,parent,false)
    )

    override fun getItemCount() = countries.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    class CountryViewHolder(view:View):RecyclerView.ViewHolder(view)
    {
        private val countryName = view.name
        private val countryCapital = view.capital
        private val imageView = view.imageView
        private val progressDrawable = getProgressDrawable(view.context)

       fun bind(country:Country)
       {
         countryName.text = country.countryName
         countryCapital.text = country.capital
         // creating an extension function for an imageView
           country.flag?.let { imageView.loadImage(it,progressDrawable) }
       }
    }

    fun updateCountries(newCountries:List<Country>)
    {
      countries.clear()
      countries.addAll(newCountries)
      notifyDataSetChanged()
    }
}