package com.mahdivajdi.myweather2.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahdivajdi.myweather2.databinding.ListItemHourlyForecastBinding
import com.mahdivajdi.myweather2.domain.HourlyForecastDomainModel
import com.mahdivajdi.myweather2.ui.setIcon
import com.mahdivajdi.myweather2.ui.getTemp
import com.mahdivajdi.myweather2.ui.setTime

class HourlyForecastListAdapter(
    private val dataSet: List<HourlyForecastDomainModel>,
    private val tempUnit: String,
) :
    RecyclerView.Adapter<HourlyForecastListAdapter.HourlyForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        return HourlyForecastViewHolder(
            ListItemHourlyForecastBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        holder.bind(dataSet[holder.adapterPosition], tempUnit)
    }

    override fun getItemCount() = dataSet.size

    class HourlyForecastViewHolder(private val binding: ListItemHourlyForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hourly: HourlyForecastDomainModel, tempUnit: String) {
            binding.apply {
                textViewHourlyTime.setTime(hourly.date)
                textViewHourlyTemp.text = "${getTemp(hourly.temp, tempUnit)}°"
                imageViewHourlyIcon.setIcon(hourly.icon)
            }
        }
    }

}

