package com.mahdivajdi.modernweather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahdivajdi.modernweather.databinding.ListItemDailyForecastBinding
import com.mahdivajdi.modernweather.domain.DailyForecastDomainModel
import com.mahdivajdi.modernweather.ui.getTemp
import com.mahdivajdi.modernweather.ui.setDate
import com.mahdivajdi.modernweather.ui.setIcon

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