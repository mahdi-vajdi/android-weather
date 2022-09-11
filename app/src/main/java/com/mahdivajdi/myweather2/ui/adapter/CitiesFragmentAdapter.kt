package com.mahdivajdi.myweather2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahdivajdi.myweather2.databinding.ListItemCityBinding
import com.mahdivajdi.myweather2.domain.CityDomainModel

class CitiesFragmentAdapter(private val deleteButtonClicked: (CityDomainModel) -> Unit) :
    ListAdapter<CityDomainModel, CitiesFragmentAdapter.CitiesViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        return CitiesViewHolder(
            ListItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        holder.bind(getItem(position),
            deleteButtonClicked)
    }

    class CitiesViewHolder(private val binding: ListItemCityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(city: CityDomainModel, deleteButtonClicked: (CityDomainModel) -> Unit) {
            binding.apply {
                textViewCitiesName.text = "${city.cityName}, ${city.county}"
                textViewCitiesCountry.text = "${city.state}, ${city.country}"
                buttonCitiesDelete.setOnClickListener { deleteButtonClicked(city) }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<CityDomainModel>() {
            override fun areItemsTheSame(
                oldItem: CityDomainModel,
                newItem: CityDomainModel,
            ): Boolean {
                return (oldItem.latitude == newItem.latitude) && (oldItem.longitude == newItem.longitude)
            }

            override fun areContentsTheSame(
                oldItem: CityDomainModel,
                newItem: CityDomainModel,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}