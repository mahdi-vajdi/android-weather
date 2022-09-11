package com.mahdivajdi.myweather2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahdivajdi.myweather2.databinding.ListItemAddCityBinding
import com.mahdivajdi.myweather2.domain.CityDomainModel

class AddCityFragmentAdapter(private val buttonClicked: (CityDomainModel) -> Unit) :
    ListAdapter<CityDomainModel, AddCityFragmentAdapter.AddCityViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddCityViewHolder {
        return AddCityViewHolder(
            ListItemAddCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AddCityViewHolder, position: Int) {
        holder.bind(getItem(position), buttonClicked)
    }


    class AddCityViewHolder(private val binding: ListItemAddCityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(city: CityDomainModel, buttonClicked: (CityDomainModel) -> Unit) {
            binding.apply {
                textViewAddCityName.text = "${city.cityName}, ${city.county}"
                textViewAddCityCounty.text = "${city.state}, ${city.country}"
                buttonAddCity.setOnClickListener { buttonClicked(city) }
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