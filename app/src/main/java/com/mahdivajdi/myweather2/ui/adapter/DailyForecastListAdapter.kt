package com.mahdivajdi.myweather2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahdivajdi.myweather2.databinding.ListItemDailyForecastBinding
import com.mahdivajdi.myweather2.domain.DailyForecastDomainModel
import com.mahdivajdi.myweather2.ui.getTemp
import com.mahdivajdi.myweather2.ui.setDate
import com.mahdivajdi.myweather2.ui.setIcon

class DailyForecastListAdapter(private val dataSet: List<DailyForecastDomainModel>, private val tempUnit: String) :
    RecyclerView.Adapter<DailyForecastListAdapter.DailyForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        return DailyForecastViewHolder(
            ListItemDailyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.bind(dataSet[holder.adapterPosition], tempUnit)
    }

    override fun getItemCount() = dataSet.size

    class DailyForecastViewHolder(private val binding: ListItemDailyForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(daily: DailyForecastDomainModel, tempUnit: String) {
                binding.apply {
                    textViewDailyForecastDay.setDate(daily.date)
                    textViewDailyForecstDetail.text = daily.detail
                    imageViewDailyForecastIcon.setIcon(daily.icon)
                    textViewDailyForecstMinTemp.text = "${getTemp(daily.minTemp, tempUnit)}°"
                    textViewDailyForecstMaxTemp.text = "${getTemp(daily.maxTemp, tempUnit)}°"
                }
            }

    }
}